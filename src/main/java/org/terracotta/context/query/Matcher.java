/*
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved.
 */
package org.terracotta.context.query;

import java.lang.reflect.Method;

/**
 * A matching object that can be used to filter the node-set in a context query
 * chain.
 * 
 * @param <T> the enclosing type of the objects that match
 */
public abstract class Matcher<T> {
  
  private Class<? extends T> boundType = (Class<? extends T>) getSafeType(getClass());
   
  private static <T extends Matcher<?>> Class<?> getSafeType(Class<T> fromClass) {
    for (Class<? super T> c = fromClass; c != Object.class; c = c.getSuperclass()) {
        for (Method method : c.getDeclaredMethods()) {
            if (method.getName().equals("matchesSafely")
              && method.getParameterTypes().length == 1
              && !method.isSynthetic()) {
                return method.getParameterTypes()[0];
            }
        }
    }
    throw new AssertionError();
  }

  /**
   * Returns true if this object matches successfully.
   * <p>
   * This method checks for a type match against the erased type of this matcher
   * and then defers to the {@link #matchesSafely(Object)} of this matcher with the
   * type-checked and cast object.
   * 
   * @param object object to be checked
   * @return {@code true} if the object matches
   */
  public final boolean matches(Object object) {
    if (boundType.isAssignableFrom(object.getClass())) {
      return matchesSafely(boundType.cast(object));
    } else {
      return false;
    }
  }
  
  /**
   * Returns {@code true} if the supplied object matches against this matcher.
   * 
   * @param object object to check for a match
   * @return {@code true} on a match
   */
  protected abstract boolean matchesSafely(T object);
}
