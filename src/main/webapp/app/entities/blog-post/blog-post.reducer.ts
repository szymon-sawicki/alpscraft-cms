import axios from 'axios';
import { createAsyncThunk, isFulfilled, isPending } from '@reduxjs/toolkit';
import { ASC } from 'app/shared/util/pagination.constants';
import { cleanEntity } from 'app/shared/util/entity-utils';
import { EntityState, IQueryParams, createEntitySlice, serializeAxiosError } from 'app/shared/reducers/reducer.utils';
import { IBlogPost, defaultValue } from 'app/shared/model/blog-post.model';

const initialState: EntityState<IBlogPost> = {
  loading: false,
  errorMessage: null,
  entities: [],
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

const apiUrl = 'api/blog-posts';

// Actions

export const getEntities = createAsyncThunk(
  'blogPost/fetch_entity_list',
  async ({ sort }: IQueryParams) => {
    const requestUrl = `${apiUrl}?${sort ? `sort=${sort}&` : ''}cacheBuster=${new Date().getTime()}`;
    return axios.get<IBlogPost[]>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const getEntity = createAsyncThunk(
  'blogPost/fetch_entity',
  async (id: string | number) => {
    const requestUrl = `${apiUrl}/${id}`;
    return axios.get<IBlogPost>(requestUrl);
  },
  { serializeError: serializeAxiosError },
);

export const createEntity = createAsyncThunk(
  'blogPost/create_entity',
  async (entity: IBlogPost, thunkAPI) => {
    console.log('[CREATE] Original entity:', entity);

    // Create a proper entity for the API - using the DTO format expected by the server
    const apiEntity = {
      title: entity.title,
      content: entity.content,
      categoryId: entity.category?.id || null,
      categoryName: entity.category?.name || null,
      authorId: entity.author?.id || null,
      authorUsername: entity.author?.login || null,
    };

    console.log('[CREATE] API entity to be sent:', apiEntity);
    console.log('[CREATE] API URL:', apiUrl);

    try {
      console.log('[CREATE] Sending request to:', apiUrl);
      const result = await axios.post<IBlogPost>(apiUrl, apiEntity);
      console.log('[CREATE] Response status:', result.status);
      console.log('[CREATE] Response data:', result.data);
      console.log('[CREATE] Headers:', result.headers);
      thunkAPI.dispatch(getEntities({}));
      return result;
    } catch (error) {
      console.error('[CREATE] Error creating entity:', error);
      if (error.response) {
        console.error('[CREATE] Error response data:', error.response.data);
        console.error('[CREATE] Error response status:', error.response.status);
        console.error('[CREATE] Error response headers:', error.response.headers);
      }
      throw error;
    }
  },
  { serializeError: serializeAxiosError },
);

export const updateEntity = createAsyncThunk(
  'blogPost/update_entity',
  async (entity: IBlogPost, thunkAPI) => {
    console.log('[UPDATE] Original entity:', entity);

    // Create a proper entity for the API - using the DTO format expected by the server
    const apiEntity = {
      id: entity.id,
      title: entity.title,
      content: entity.content,
      categoryId: entity.category?.id || null,
      categoryName: entity.category?.name || null,
      authorId: entity.author?.id || null,
      authorUsername: entity.author?.login || null,
    };

    console.log('[UPDATE] API entity to be sent:', apiEntity);
    console.log('[UPDATE] API URL:', `${apiUrl}/${entity.id}`);

    try {
      console.log('[UPDATE] Sending request to:', `${apiUrl}/${entity.id}`);
      const result = await axios.put<IBlogPost>(`${apiUrl}/${entity.id}`, apiEntity);
      console.log('[UPDATE] Response status:', result.status);
      console.log('[UPDATE] Response data:', result.data);
      console.log('[UPDATE] Headers:', result.headers);
      thunkAPI.dispatch(getEntities({}));
      return result;
    } catch (error) {
      console.error('[UPDATE] Error updating entity:', error);
      if (error.response) {
        console.error('[UPDATE] Error response data:', error.response.data);
        console.error('[UPDATE] Error response status:', error.response.status);
        console.error('[UPDATE] Error response headers:', error.response.headers);
      }
      throw error;
    }
  },
  { serializeError: serializeAxiosError },
);

export const partialUpdateEntity = createAsyncThunk(
  'blogPost/partial_update_entity',
  async (entity: IBlogPost, thunkAPI) => {
    const result = await axios.patch<IBlogPost>(`${apiUrl}/${entity.id}`, cleanEntity(entity));
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

export const deleteEntity = createAsyncThunk(
  'blogPost/delete_entity',
  async (id: string | number, thunkAPI) => {
    const requestUrl = `${apiUrl}/${id}`;
    const result = await axios.delete<IBlogPost>(requestUrl);
    thunkAPI.dispatch(getEntities({}));
    return result;
  },
  { serializeError: serializeAxiosError },
);

// slice

export const BlogPostSlice = createEntitySlice({
  name: 'blogPost',
  initialState,
  extraReducers(builder) {
    builder
      .addCase(getEntity.fulfilled, (state, action) => {
        state.loading = false;
        state.entity = action.payload.data;
      })
      .addCase(deleteEntity.fulfilled, state => {
        state.updating = false;
        state.updateSuccess = true;
        state.entity = {};
      })
      .addMatcher(isFulfilled(getEntities), (state, action) => {
        const { data } = action.payload;

        return {
          ...state,
          loading: false,
          entities: data.sort((a, b) => {
            if (!action.meta?.arg?.sort) {
              return 1;
            }
            const order = action.meta.arg.sort.split(',')[1];
            const predicate = action.meta.arg.sort.split(',')[0];
            return order === ASC ? (a[predicate] < b[predicate] ? -1 : 1) : b[predicate] < a[predicate] ? -1 : 1;
          }),
        };
      })
      .addMatcher(isFulfilled(createEntity, updateEntity, partialUpdateEntity), (state, action) => {
        state.updating = false;
        state.loading = false;
        state.updateSuccess = true;
        state.entity = action.payload.data;
      })
      .addMatcher(isPending(getEntities, getEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.loading = true;
      })
      .addMatcher(isPending(createEntity, updateEntity, partialUpdateEntity, deleteEntity), state => {
        state.errorMessage = null;
        state.updateSuccess = false;
        state.updating = true;
      });
  },
});

export const { reset } = BlogPostSlice.actions;

// Reducer
export default BlogPostSlice.reducer;
