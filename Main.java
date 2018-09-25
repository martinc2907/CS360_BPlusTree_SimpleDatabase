import java.io.*;
import java.util.*;


public class Main {
	public static void main(String args[]) {
		Node node = new Node();

		node.add(5,1);
		node.add(9,2);
		node.add(1,3);
		node.add(3,4);
		node.add(4,5);
		node.add(59,6);
		node.add(65,7);
		node.add(45,8);
		node.add(89,9);
		node.add(29,10);
		node.add(68,11);
		node.add(108,12);
		node.add(165,14);
		node.add(298,15);
		node.add(219,16);
		node.add(569,17);
		node.add(37,18);
		node.add(47,19);
		node.add(1,69);

		node.remove(5,1);
		node.remove(9,2);
		node.remove(1,3);
		node.remove(3,4);
		node.remove(4,5);
		node.remove(59,6);
		node.remove(65,7);
		node.remove(45,8);
		node.remove(89,9);
		node.remove(29,10);
		node.remove(68,11);
		node.remove(108,12);
		node.remove(165,14);
		node.remove(298,15);
		node.remove(219,16);
		node.remove(569,17);
		node.remove(37,18);
		node.remove(47,19);


		node.print(2);
		System.out.println();
		printstuff(1,node);


	}


	public static void printstuff(int key,Node tree){
		Node search = tree.search(key);
		LinkedList<Integer> list;
		while(search!=null){
			for(int i = 0; i < search.dataCount; i++){
				System.out.print(search.data[i] + ": ");
				list = search.valueList[i];
				//PRINT ALL IN LIST.
				for(int integer : list){
					System.out.print(integer + " ");
				}
				System.out.println();
			}
			search = search.nextLeaf;
		}
	}
}