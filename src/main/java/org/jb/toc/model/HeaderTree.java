package org.jb.toc.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import org.jb.toc.HeaderType;

/**
 * Stores markdown header indexes in the n-ary tree. Use {@link #add(HeaderType, String)} to add a new next node.
 * Works fine with correct headers and sub-headers order. Use {@link #traverse()} to traverse built tree. This algorithm uses preorder traversal.
 * Input:
 * # My Project 1
 * ## Idea 1
 * ## Idea 1
 * ### Idea 2
 * ### Idea 2
 * ### Idea 2
 * #### Idea 3
 * #### Idea 3
 * ##### Idea 4
 * ## Idea 5
 * ## Idea 5
 * ### Idea 6
 * ### Idea 6
 * # My Project 2
 * ## Idea 1
 * ## Idea 1
 * ##### Idea 2
 * ##### Idea 2
 * # My Project 3
 * ## Idea 1
 * ### Idea 2
 * #### Idea 3
 * ##### Idea 4
 * ###### Idea 5
 * # My Project 4
 *
 * Output:
 * 1. [My Project 1](#my-project-1)
 * 	1. [Idea 1](#idea-1)
 * 	2. [Idea 1](#idea-1)
 * 		1. [Idea 2](#idea-2)
 * 		2. [Idea 2](#idea-2)
 * 		3. [Idea 2](#idea-2)
 * 			1. [Idea 3](#idea-3)
 * 			2. [Idea 3](#idea-3)
 * 				1. [Idea 4](#idea-4)
 * 	3. [Idea 5](#idea-5)
 * 	4. [Idea 5](#idea-5)
 * 		1. [Idea 6](#idea-6)
 * 		2. [Idea 6](#idea-6)
 * 2. [My Project 2](#my-project-2)
 * 	1. [Idea 1](#idea-1)
 * 	2. [Idea 1](#idea-1)
 * 				1. [Idea 2](#idea-2)
 * 				2. [Idea 2](#idea-2)
 * 3. [My Project 3](#my-project-3)
 * 	1. [Idea 1](#idea-1)
 * 		1. [Idea 2](#idea-2)
 * 			1. [Idea 3](#idea-3)
 * 				1. [Idea 4](#idea-4)
 * 					1. [Idea 5](#idea-5)
 * 4. [My Project 4](#my-project-4)
 */
public class HeaderTree {

  private static final String SEPARATOR = ". ";
  private static final char SHIFT = '\t';

  private TreeNode root = new TreeNode(HeaderType.FIRST_LEVEL, null, new LinkedList<>(), "ROOT");
  private TreeNode pointer = root;

  private static class TreeNode {
    int siblingsCount;
    String content;
    HeaderType type;
    TreeNode parent;
    Deque<TreeNode> children;

    TreeNode(HeaderType type, TreeNode parent, Deque<TreeNode> children, String content) {
	  this.content = content;
	  this.type = type;
	  this.parent = parent;
	  this.children = children;
	}

	boolean hasChildren() {
	  return !children.isEmpty();
	}

	void addChild(TreeNode node) {
	  children.push(node);
	}

	TreeNode peek() {
	  return children.peek();
	}
  }

  public TreeNode add(HeaderType headerType, String content) {
	if (pointer.type == headerType) {
      if (pointer.parent != null) {
		pointer.parent.siblingsCount++;
		TreeNode curr = new TreeNode(headerType, pointer.parent, new LinkedList<>(), pointer.parent.siblingsCount + SEPARATOR + content);
		pointer.parent.children.add(curr);
		pointer = curr;
	  } else {
		TreeNode curr = new TreeNode(headerType, pointer, new LinkedList<>(), "1. " + content);
		pointer.children.add(curr);
		pointer.siblingsCount++;
		pointer = curr;
	  }
	} else if (pointer.type.diff(headerType) > 0) {
      if (!pointer.hasChildren()) {
        pointer.siblingsCount++;
		TreeNode curr = new TreeNode(headerType, pointer, new LinkedList<>(), pointer.siblingsCount + SEPARATOR + content);
		pointer.addChild(curr);
        pointer = curr;
	  }
	} else {
      TreeNode lastNode = pointer;
	  while (lastNode.type.diff(headerType) != 0) {
	    if (lastNode.parent != null && lastNode.parent.parent != null) {
	      lastNode = lastNode.parent;
		} else {
	      break;
		}
	  }
	  pointer = lastNode;
	  lastNode.parent.siblingsCount++;
	  TreeNode curr = new TreeNode(headerType, lastNode.parent, new LinkedList<>(), lastNode.parent.siblingsCount + SEPARATOR + content);
	  lastNode.parent.children.add(curr);
	  pointer = curr;
	}
	return pointer;
  }

  public void traverse() {
    Deque<TreeNode> nodes = new ArrayDeque<>();
    nodes.push(root);

    while (!nodes.isEmpty()) {
	  TreeNode curr = nodes.poll();
	  if (curr != null) {
		int childrenSize = curr.children.size();
		for (int i = 0; i < curr.type.starts().length() - 1; i++) {
		  System.out.print(SHIFT);
		}
		if (curr.parent != null) {
		  System.out.println(curr.content);
		}
		for (int i = 0; i < childrenSize; i++) {
		  TreeNode item = curr.children.pollLast();
		  nodes.push(item);
		}
		for (TreeNode node : curr.children) {
		  nodes.push(node);
		}
	  }
	}
  }

}
