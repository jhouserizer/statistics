/*
 * Copyright Terracotta, Inc.
 * Copyright IBM Corp. 2024, 2025
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terracotta.context;

import org.hamcrest.core.IsCollectionContaining;
import org.junit.Assert;
import org.terracotta.context.annotations.ContextAttribute;
import org.terracotta.context.annotations.ContextChild;
import org.terracotta.context.annotations.ContextParent;

import java.util.Collections;
import java.util.Set;

import static org.hamcrest.core.IsSame.sameInstance;
import static org.terracotta.context.query.Matchers.attributes;
import static org.terracotta.context.query.Matchers.context;
import static org.terracotta.context.query.Matchers.hasAttribute;
import static org.terracotta.context.query.QueryBuilder.queryBuilder;

/**
 * @author cdennis
 */
public final class ContextTestUtils {

  private ContextTestUtils() {
    //static
  }

  @SuppressWarnings("unchecked")
  public static void validateAssociation(ContextManager manager, Object parent, Object child) {
    TreeNode parentNode = manager.queryForSingleton(queryBuilder().descendants().filter(context(attributes(hasAttribute("this", parent)))).build());
    TreeNode childNode = manager.queryForSingleton(queryBuilder().descendants().filter(context(attributes(hasAttribute("this", child)))).build());
    Assert.assertThat((Set<TreeNode>) parentNode.getChildren(), IsCollectionContaining.hasItem(sameInstance(childNode)));
  }

  public static void validateNoAssociation(ContextManager manager, Object parent, Object child) {
    TreeNode parentNode = manager.queryForSingleton(queryBuilder().descendants().filter(context(attributes(hasAttribute("this", parent)))).build());
    Assert.assertTrue(queryBuilder().children().filter(context(attributes(hasAttribute("this", child)))).build().execute(Collections.singleton(parentNode)).isEmpty());
  }

  @ContextAttribute("this")
  public static class PublicAnnotations {
    @ContextChild
    public Object child;

    @ContextParent
    public Object parent;
  }

  @ContextAttribute("this")
  public static class PrivateAnnotations {
    @ContextChild
    private Object child;

    @ContextParent
    private Object parent;

    public void setChild(Object child) {
      this.child = child;
    }

    public void setParent(Object parent) {
      this.parent = parent;
    }
  }

  @ContextAttribute("this")
  public static class NoAnnotations {}
}
