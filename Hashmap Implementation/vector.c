#include "vector.h"
#include <string.h>

/**
 * Dynamically allocates a new vector.
 * @param elem_copy_func func which copies the element stored in the vector
 * (returns dynamically allocated copy).
 * @param elem_cmp_func func which is used to compare elements stored in the
 * vector.
 * @param elem_free_func func which frees elements stored in the vector.
 * @return pointer to dynamically allocated vector.
 * @if_fail return NULL.
 */
vector *vector_alloc (vector_elem_cpy elem_copy_func,
                      vector_elem_cmp elem_cmp_func,
                      vector_elem_free elem_free_func)
{
  vector *new_vec = malloc (sizeof (vector));
  if (new_vec == NULL || elem_copy_func == NULL || elem_cmp_func == NULL
      || elem_free_func == NULL)
    {
      return NULL;
    }
  new_vec->capacity = VECTOR_INITIAL_CAP;
  new_vec->size = 0;
  new_vec->data = malloc (sizeof (void *) * new_vec->capacity);
  if (new_vec->data == NULL)
    {
      free (new_vec);
      return NULL;
    }
  new_vec->elem_copy_func = elem_copy_func;
  new_vec->elem_cmp_func = elem_cmp_func;
  new_vec->elem_free_func = elem_free_func;
  return new_vec;
}

/**
 * Frees a vector and the elements the vector itself allocated.
 * @param p_vector pointer to dynamically allocated pointer to vector.
 */
void vector_free (vector **p_vector)
{
  if( p_vector!= NULL)
    {
      // frees the elements of the vector
      for (size_t i = 0; i < (*p_vector)->size; i++)
        {
          void *tmp = vector_at(*p_vector, i);
          (*p_vector)->elem_free_func (&tmp);
        }
      free ((*p_vector)->data);  // frees data
      free (*p_vector);  // frees vector
      (*p_vector) = NULL;
      p_vector = NULL;
    }
}

/**
 * Returns the element at the given index.
 * @param vector pointer to a vector.
 * @param ind the index of the element we want to get.
 * @return the element at the given index if exists (the element itself,
 * not a copy of it),
 * NULL otherwise.
 */
void *vector_at (const vector *vector, size_t ind)
{
  if (vector == NULL || ind > vector->size)
    {
      return NULL;
    }
  return vector->data[ind];
}

/**
 * Gets a value and checks if the value is in the vector.
 * @param vector a pointer to vector.
 * @param value the value to look for.
 * @return the index of the given value if it is in the vector
 * ([0, vector_size - 1]).
 * Returns -1 if no such value in the vector.
 */
int vector_find (const vector *vector, const void *value)
{
  if (vector!= NULL)
    {
      for (size_t i = 0; i < vector->size; i++)
        {
          if (vector->elem_cmp_func (value, vector->data[i]))
            {
              return i;
            }
        }
    }
  return -1;
}

/**
 * Adds a new value to the back (index vector_size) of the vector.
 * @param vector a pointer to vector.
 * @param value the value to be added to the vector.
 * @return 1 if the adding has been done successfully, 0 otherwise.
 */
int vector_push_back (vector *vector, const void *value)
{
  if (vector == NULL)
    {
      return 0;
    }
  // creates copy
  vector->data[vector->size] = vector->elem_copy_func (value);
  vector->size++; // updates size
  //  if load factor too big updates capacity
  if (vector_get_load_factor (vector) > VECTOR_MAX_LOAD_FACTOR)
    {
      void **tmp = realloc (vector->data,
                            sizeof (void *) * (vector->capacity*2));
      if (tmp == NULL)
        {
          return 0;
        }
      vector->data = tmp;
      vector->capacity = vector->capacity * VECTOR_GROWTH_FACTOR;
    }
  return 1;
}

/**
 * This function returns the load factor of the vector.
 * @param vector a vector.
 * @return the vector's load factor, -1 if the function failed.
 */
double vector_get_load_factor (const vector *vector)
{
  if (vector == NULL)
    {
      return -1;
    }
  return (float) vector->size / vector->capacity;
}

/**
 * Removes the element at the given index from the vector. alters the indices
 * of the remaining elements so that there are no empty indices in the range
 * [0, size-1] (inclusive).
 * @param vector a pointer to vector.
 * @param ind the index of the element to be removed.
 * @return 1 if the removing has been done successfully, 0 otherwise.
 */
int vector_erase (vector *vector, size_t ind)
{
  if (ind > vector->size - 1)
    {
      return 0;
    }
  // frees the element to be erased
  void *tmp = vector_at(vector, ind);
  vector->elem_free_func(&tmp);
  // updates data indices
  for (size_t i = ind; i < vector->size - 1; i++)
    {
      vector->data[i] = vector->data[i+1];
    }
  vector->size--;
  // if load factor too small updates capacity
  if (vector_get_load_factor (vector) < VECTOR_MIN_LOAD_FACTOR)
    {
      void **tmp2 = realloc (vector->data,
                            sizeof (void *) * (vector->capacity/2));
      if (tmp2 == NULL)
        {
          return 0;
        }
      vector->data = tmp2;
      vector->capacity = vector->capacity / 2;
    }
  return 1;
}

/**
 * Deletes all the elements in the vector.
 * @param vector vector a pointer to vector.
 */
void vector_clear (vector *vector)
{
  if (vector != NULL)
    {
      for (int i = (int) vector->size - 1; i >= 0; i--)
        {
          vector_erase (vector, i);
        }
    }
}



