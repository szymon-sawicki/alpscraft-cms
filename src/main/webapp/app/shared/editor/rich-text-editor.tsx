import React, { useState, useRef, useEffect } from 'react';
import ReactQuill from 'react-quill';
import 'react-quill/dist/quill.snow.css';
import { Button, Modal, ModalHeader, ModalBody, ModalFooter, FormGroup, Label, Input, Progress, Alert } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { useDropzone } from 'react-dropzone';
import axios from 'axios';
import { Translate } from 'react-jhipster';

interface RichTextEditorProps {
  value: string;
  onChange: (content: string) => void;
  placeholder?: string;
}

export const RichTextEditor = ({ value, onChange, placeholder }: RichTextEditorProps) => {
  const [editorHtml, setEditorHtml] = useState(value || '');
  const [imageModalOpen, setImageModalOpen] = useState(false);
  const [uploadProgress, setUploadProgress] = useState(0);
  const [uploadError, setUploadError] = useState('');
  const [isUploading, setIsUploading] = useState(false);
  const [imageUrl, setImageUrl] = useState('');
  const quillRef = useRef<ReactQuill>(null);

  useEffect(() => {
    setEditorHtml(value || '');
  }, [value]);

  const handleChange = (html: string) => {
    setEditorHtml(html);
    onChange(html);
  };

  const handleImageUpload = async (files: File[]) => {
    if (files && files.length > 0) {
      const file = files[0];

      // Check file size (limit to 5MB)
      if (file.size > 5 * 1024 * 1024) {
        setUploadError('File size exceeds 5MB limit');
        return;
      }

      // Check file type
      if (!file.type.includes('image/')) {
        setUploadError('Only image files are allowed');
        return;
      }

      setIsUploading(true);
      setUploadError('');

      const formData = new FormData();
      formData.append('file', file);

      try {
        const response = await axios.post('/api/file-upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
          onUploadProgress: progressEvent => {
            const percentCompleted = Math.round((progressEvent.loaded * 100) / (progressEvent.total || 1));
            setUploadProgress(percentCompleted);
          },
        });

        // Get the URL from the response
        const imageUrl = response.data.url;
        setImageUrl(imageUrl);

        setIsUploading(false);
        setUploadProgress(0);
      } catch (error) {
        console.error('Upload error:', error);
        setUploadError('Error uploading image. Please try again.');
        setIsUploading(false);
        setUploadProgress(0);
      }
    }
  };

  const insertImage = () => {
    const editor = quillRef.current?.getEditor();
    if (editor && imageUrl) {
      const range = editor.getSelection();
      editor.insertEmbed(range?.index || 0, 'image', imageUrl);
      setImageModalOpen(false);
      setImageUrl('');
    }
  };

  const { getRootProps, getInputProps } = useDropzone({
    accept: {
      'image/*': [],
    },
    onDrop: handleImageUpload,
    multiple: false,
  });

  // Custom toolbar with image upload button
  const modules = {
    toolbar: {
      container: [
        [{ header: [1, 2, 3, 4, 5, 6, false] }],
        ['bold', 'italic', 'underline', 'strike'],
        [{ list: 'ordered' }, { list: 'bullet' }],
        [{ align: [] }],
        ['link', 'image'],
        ['clean'],
      ],
      handlers: {
        image: () => setImageModalOpen(true),
      },
    },
  };

  const formats = ['header', 'bold', 'italic', 'underline', 'strike', 'list', 'bullet', 'align', 'link', 'image'];

  return (
    <div className="rich-text-editor">
      <ReactQuill
        ref={quillRef}
        value={editorHtml}
        onChange={handleChange}
        modules={modules}
        formats={formats}
        placeholder={placeholder || 'Write your content here...'}
      />

      {/* Image Upload Modal */}
      <Modal isOpen={imageModalOpen} toggle={() => setImageModalOpen(!imageModalOpen)}>
        <ModalHeader toggle={() => setImageModalOpen(false)}>
          <Translate contentKey="editor.insertImage">Insert Image</Translate>
        </ModalHeader>
        <ModalBody>
          {uploadError && (
            <Alert color="danger" timeout={300}>
              {uploadError}
            </Alert>
          )}

          <div {...getRootProps({ className: 'dropzone p-5 mb-3 text-center border rounded' })}>
            <input {...getInputProps()} />
            <p>
              <FontAwesomeIcon icon="upload" className="me-2" />
              <Translate contentKey="editor.dragOrClick">Drag and drop an image here, or click to select an image</Translate>
            </p>
          </div>

          {isUploading && (
            <div className="mt-3">
              <Progress value={uploadProgress} />
              <p className="text-center mt-2">Uploading: {uploadProgress}%</p>
            </div>
          )}

          {imageUrl && (
            <div className="mt-3">
              <h5>
                <Translate contentKey="editor.preview">Preview</Translate>
              </h5>
              <img src={imageUrl} alt="Preview" className="img-fluid mb-3" />
            </div>
          )}

          {!isUploading && imageUrl && (
            <FormGroup>
              <Label for="imageAlt">
                <Translate contentKey="editor.altText">Alt Text</Translate>
              </Label>
              <Input type="text" id="imageAlt" placeholder="Description of image for accessibility" />
            </FormGroup>
          )}
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={() => setImageModalOpen(false)}>
            <Translate contentKey="entity.action.cancel">Cancel</Translate>
          </Button>
          <Button color="primary" onClick={insertImage} disabled={!imageUrl || isUploading}>
            <Translate contentKey="entity.action.insert">Insert</Translate>
          </Button>
        </ModalFooter>
      </Modal>
    </div>
  );
};

export default RichTextEditor;
