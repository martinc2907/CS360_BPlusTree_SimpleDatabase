import java.io.*;
import java.util.*;


public class Main {
	public static void main(String args[]) {
		Node node = new Node();

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
	}
}