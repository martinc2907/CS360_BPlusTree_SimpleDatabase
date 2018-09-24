import java.io.*;
import java.util.*;


public class Main {
	public static void main(String args[]) {
		Node node = new Node();

		// node.add(3,0);
		// node.add(5,1);
		// node.add(5,2);
		// node.add(4,0);

		node.add(5,0);
		node.add(9,1);
		node.add(1,2);
		node.add(3,3);
		node.add(4,4);
		node.add(59,5);
		node.add(65,6);
		node.add(45,7);
		node.add(89,8);
		node.add(29,9);
		node.add(68,10);
		node.add(108,11);
		node.add(165,12);
		node.add(298,13);
		node.add(298,54);//
		node.add(219,14);
		node.add(569,15);
		node.add(37,16);
		node.add(47,17);
		node.add(1,69);//
		node.add(45,43);//

		node.print(2);

		printstuff(1,node);
	}


	public static void printstuff(int key,Node tree){
		Node search = tree.search(key);
		LinkedList<Integer> list;
		while(search!=null){
			for(int i = 0; i < search.dataCount; i++){
				System.out.print(search.data[i] + " ");
				list = search.valueList[i];
				//PRINT ALL IN LIST.
				for(int integer : list){
					System.out.print(integer + " ");
				}
				System.out.println();
			}
			search = search.nextLeaf;
		}
		System.out.println("print stuff done");
	}
}