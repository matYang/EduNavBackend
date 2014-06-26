package BaseModule.model.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SDTree<T> {
	
	private T head;
	private SDTree<T> parent = null;
	private ArrayList<SDTree<T>> leafs = new ArrayList<SDTree<T>>();
	private HashMap<T, SDTree<T>> locate = new HashMap<T, SDTree<T>>();

	public SDTree(T head) {
		this.head = head;
		locate.put(head, this);
	}

	public void addLeaf(T root, T leaf) {
		if (locate.containsKey(root)) {
			locate.get(root).addLeaf(leaf);
		} else {
			addLeaf(root).addLeaf(leaf);
		}
	}

	public SDTree<T> addLeaf(T leaf) {
		SDTree<T> t = new SDTree<T>(leaf);
		leafs.add(t);
		t.parent = this;
		t.locate = this.locate;
		locate.put(leaf, t);
		return t;
	}
	
	public SDTree<T> getRandomLeaf(){
		int index = new Random().nextInt(this.leafs.size());
		return this.leafs.get(index);
	}
	
	public T getRandomLeafVal(){
		return getRandomLeaf().getHead();
	}

	public SDTree<T> setAsParent(T parentRoot) {
		SDTree<T> t = new SDTree<T>(parentRoot);
		t.leafs.add(this);
		this.parent = t;
		t.locate = this.locate;
		t.locate.put(head, this);
		t.locate.put(parentRoot, t);
		return t;
	}

	public T getHead() {
		return head;
	}

	public SDTree<T> getTree(T element) {
		return locate.get(element);
	}

	public SDTree<T> getParent() {
		return parent;
	}

	public List<T> getSuccessors(T root) {
		List<T> successors = new ArrayList<T>();
		SDTree<T> tree = getTree(root);
		if (null != tree) {
			for (SDTree<T> leaf : tree.leafs) {
				successors.add(leaf.head);
			}
		}
		return successors;
	}

	public List<SDTree<T>> getSubTrees() {
		return leafs;
	}


	@Override
	public String toString() {
		return convertTreeTpStromg(0);
	}
	private String convertTreeTpStromg(int increment) {
		final int indent = 2;
		String s = "";
		String inc = "";
		for (int i = 0; i < increment; ++i) {
			inc = inc + " ";
		}
		s = inc + head;
		for (SDTree<T> child : leafs) {
			s += "\n" + child.convertTreeTpStromg(increment + indent);
		}
		return s;
	}
}