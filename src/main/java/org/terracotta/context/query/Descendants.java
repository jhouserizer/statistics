/*
 * Copyright Terracotta, Inc.
 * Copyright Super iPaaS Integration LLC, an IBM Company 2024
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
package org.terracotta.context.query;

import org.terracotta.context.TreeNode;

import java.util.HashSet;
import java.util.Set;

class Descendants implements Query {

  static final Query INSTANCE = new Descendants();

  @Override
  public Set<TreeNode> execute(Set<TreeNode> input) {
    Set<TreeNode> descendants = new HashSet<>();
    for (Set<TreeNode> children = Children.INSTANCE.execute(input); !children.isEmpty(); children = Children.INSTANCE.execute(children)) {
      if (!descendants.addAll(children)) {
        break;
      }
    }
    return descendants;
  }

  @Override
  public String toString() {
    return "descendants";
  }
}
