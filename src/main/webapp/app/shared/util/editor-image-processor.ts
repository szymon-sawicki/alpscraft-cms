import axios from 'axios';

/**
 * Processes the content of a rich text editor to extract and upload base64 images
 * and replace them with URLs to the uploaded files.
 */
export const processEditorContent = async (content: string): Promise<string> => {
  if (!content) {
    return content;
  }

  // Regular expression to find base64 images in content
  const regex = /<img[^>]+src="(data:image\/[^;]+;base64,[^"]+)"[^>]*>/g;

  let match;
  let processedContent = content;

  // Create an array to hold all the pending uploads
  const uploadPromises = [];
  const replacements = [];

  // Extract all base64 images and queue them for upload
  while ((match = regex.exec(content)) !== null) {
    const fullImgTag = match[0];
    const base64Data = match[1];

    // Skip if already processed
    if (base64Data.startsWith('http')) {
      continue;
    }

    // Queue the upload
    uploadPromises.push(
      uploadBase64Image(base64Data).then(fileUrl => {
        replacements.push({
          original: fullImgTag,
          replacement: fullImgTag.replace(base64Data, fileUrl),
        });
      }),
    );
  }

  // Wait for all uploads to complete
  if (uploadPromises.length > 0) {
    await Promise.all(uploadPromises);

    // Apply all replacements
    replacements.forEach(({ original, replacement }) => {
      processedContent = processedContent.replace(original, replacement);
    });
  }

  return processedContent;
};

/**
 * Uploads a base64 image to the server
 * @param base64Data the base64 encoded image data
 * @returns the URL of the uploaded file
 */
const uploadBase64Image = async (base64Data: string): Promise<string> => {
  try {
    const response = await axios.post('/api/files/upload-base64', {
      content: base64Data,
      fileName: generateFileName(),
    });

    return response.data.fileUrl;
  } catch (error) {
    console.error('Error uploading image:', error);
    // Return the original base64 if upload fails
    return base64Data;
  }
};

/**
 * Generates a random file name
 */
const generateFileName = (): string => {
  const timestamp = new Date().getTime();
  const random = Math.floor(Math.random() * 1000);
  return `image_${timestamp}_${random}.jpg`;
};
