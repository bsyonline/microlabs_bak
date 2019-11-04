/*
 * Copyright (C) 2019 bsyonline
 */
package com.rolex.tree;

/**
 * @author rolex
 * @since 2019
 */
public class BinaryTreeDemo {

    public String preOrder(BinaryTree tree) {
        if (tree == null) {
            return "";
        }
        String s = "";
        s += tree.getValue() + " ";
        s += preOrder(tree.getLeft());
        s += preOrder(tree.getRight());
        return s;
    }

    public String middleOrder(BinaryTree tree) {
        if (tree == null) {
            return "";
        }
        String s = "";
        s += middleOrder(tree.getLeft());
        s += tree.getValue() + " ";
        s += middleOrder(tree.getRight());
        return s;
    }

    public String postOrder(BinaryTree tree) {
        if (tree == null) {
            return "";
        }
        String s = "";
        s += postOrder(tree.getLeft());
        s += postOrder(tree.getRight());
        s += tree.getValue() + " ";
        return s;
    }

    public boolean like(BinaryTree t1, BinaryTree t2) {
        if (t1 == null && t2 == null) {
            return true;
        } else {
            if (t1 != null && t2 != null) {
                return like(t1.left, t2.left) & like(t1.right, t2.right);
            } else {
                return false;
            }
        }
    }

    public static void main(String[] args) {

        BinaryTree tree4 = new BinaryTree('D', null, null);
        BinaryTree tree5 = new BinaryTree('E', null, null);
        BinaryTree tree6 = new BinaryTree('F', null, null);
        BinaryTree tree7 = new BinaryTree('G', null, null);

        BinaryTree tree3 = new BinaryTree('C', tree6, tree7);
        BinaryTree tree2 = new BinaryTree('B', tree4, tree5);
        BinaryTree tree1 = new BinaryTree('A', tree2, tree3);
        BinaryTree tree11 = new BinaryTree('a', tree2, tree3);

        BinaryTreeDemo tree = new BinaryTreeDemo();
        System.out.println(tree.preOrder(tree1));
        System.out.println(tree.middleOrder(tree1));
        System.out.println(tree.postOrder(tree1));

        System.out.println(tree.like(tree1, tree11));
    }

    static class BinaryTree {
        BinaryTree left;
        BinaryTree right;
        char value;

        public BinaryTree(char value, BinaryTree left, BinaryTree right) {
            this.value = value;
            this.left = left;
            this.right = right;
        }

        public BinaryTree getLeft() {
            return left;
        }

        public void setLeft(BinaryTree left) {
            this.left = left;
        }

        public BinaryTree getRight() {
            return right;
        }

        public void setRight(BinaryTree right) {
            this.right = right;
        }

        public char getValue() {
            return value;
        }

        public void setValue(char value) {
            this.value = value;
        }
    }
}
