import java.io.*;
import java.util.*;

// KEY = (Year of Release, No. of Votes)
// Compare YoR first, then No. of votes
// Ascending order

public class Main {

	public static LinkedList<Movie> movies = new LinkedList<Movie>();

	public static void main(String args[]) {


		Node node = new Node();
		Scanner in = new Scanner(System.in);
		boolean run_program = true;

		int menu;

		/* Initialise list of movies. */
		list_init();


		/* Command line interface */
		while(run_program){
			System.out.println("======== B+ Tree Program =======");
			System.out.println("1.  LOAD");
			System.out.println("2.  PRINT");
			System.out.println("3.  INSERT");
			System.out.println("4.  DELETE");
			System.out.println("5.  SEARCH");
			System.out.println("6.  RANGE_SEARCH");
			System.out.println("7.  EXIT");
			System.out.println("================================");
			System.out.print("SELECT MENU AND ENTER: ");

			menu = in.nextInt();
			in.nextLine();//consume newline.

			switch(menu){

				case 1://LOAD
					load(node, in);
					break;

				case 2://PRINT
					print(node);
					break;

				case 3://INSERT
					insert(node, in);
					break;

				case 4://DELETE
					delete(node, in);
					break;

				case 5://SEARCH
					search(node, in);
					break;

				case 6://RANGE_SEARCH
					range_search(node, in);
					break;

				case 7://EXIT
					run_program = false;
					break;

				default://WRONG INPUT
					break;
			}
		}
		in.close();
	}

	private static void load(Node tree, Scanner in){
		Movie movie;
		Key key;

		//UI for load.
		System.out.print("  LOAD_START_TID: ");
		int tid_start = in.nextInt();
		in.nextLine();
		System.out.print("  LOAD_END_TID: ");
		int tid_end = in.nextInt();
		in.nextLine();
		System.out.println("LOADING ...");

		//Make tid_start ~ tid_end to tree from list.
		for(int i =0; i < movies.size(); i++){
			movie = movies.get(i);
			if(movie.tid >= tid_start && movie.tid <= tid_end){
				//add to tree.
				key = new Key(movie.year_of_release, movie.no_of_votes);
				tree.add(key, movie.tid);
			}
		}

		System.out.println("B+ Tree is built.");
	}

	private static void insert(Node tree, Scanner in){
		System.out.print("TUPLE ID: ");
		int tid = in.nextInt();
		in.nextLine();

		if(tid > movies.size()){
			System.out.println("TID out of range.");
			return;
		}

		Movie movie = movies.get(tid-1);
		Key key = new Key(movie.year_of_release, movie.no_of_votes);

		tree.add(key, movie.tid);

		System.out.println("Tuple #" + tid + " is inserted.");
	}

	private static void delete(Node tree, Scanner in){
		System.out.print("TUPLE ID: ");
		int tid = in.nextInt();
		in.nextLine();

		if(tid > movies.size()){
			System.out.println("Tuple #" + tid + " doesn't exist in the tree.");
			return;
		}

		Movie movie = movies.get(tid-1);
		Key key = new Key(movie.year_of_release, movie.no_of_votes);

		boolean success = tree.remove(key, movie.tid);

		if(success){
			System.out.println("Tuple #" + tid + " is deleted.");
		}else{
			System.out.println("Tuple #" + tid + " doesn't exist in the tree.");
		}
	}

	private static void search(Node tree, Scanner in){
		System.out.print("SEARCH KEY: ");

		String line = in.nextLine();

		//remove spaces, and parentheses.
		line = line.replaceAll("\\(", "");
		line = line.replaceAll("\\)", "");
		line = line.replaceAll(" ", "");

		String[] tokens = line.split(",");

		int release_year = Integer.parseInt(tokens[0]);
		int no_of_votes = Integer.parseInt(tokens[1]);

		Key key = new Key(release_year, no_of_votes);

		Node result = tree.search(key);

		if(result == null || result.dataCount == 0){
			System.out.println("Found tuple IDs: []");
			System.out.println("KEY NOT FOUND");
			return;
		}else{
			int index;
			if(result.data[0].equals(key)){
				index = 0;
			}else{
				index = 1;
			}

			//copy tid values from list.
			int length = result.valueList[index].size();
			int[] tids = new int[length];
			for(int i =0; i < length; i++){
				tids[i] = result.valueList[index].get(i);
			}

			//print
			System.out.print("Found tuple IDs: [");
			for(int j = 0; j < length; j++){
				System.out.print(tids[j]);
				if(j != length-1){
					System.out.print(",");
				}
			}
			System.out.println("]");

			/* Find and print actual data */
			System.out.println("Attributes: <tid, movie, year of release, rating, no.of votes, director>");
			Movie movie;
			for(int k = 0; k < length; k++){
				movie = movies.get(tids[k]-1);
				System.out.print("Tuple #" + movie.tid + ":" + "<");
				System.out.print(movie.tid + ", ");
				System.out.print("\"" + movie.movie + "\", ");
				System.out.print(movie.year_of_release + ", ");
				System.out.print(movie.rating + ", ");
				System.out.print(movie.no_of_votes + ", ");
				System.out.print("\"" + movie.director + "\"");
				System.out.println(">");
			}
		}
	}

	private static void range_search(Node tree, Scanner in){
		System.out.print("SEARCH RANGE: ");

		String line = in.nextLine();

		//remove brackets, parantheses, spaces.
		line = line.replaceAll("\\(", "");
		line = line.replaceAll("\\)", "");
		line = line.replaceAll("\\]", "");
		line = line.replaceAll("\\[", "");
		line = line.replaceAll(" ", "");

		String[] tokens = line.split(",");
		
		int release_year = Integer.parseInt(tokens[0]);
		int no_of_votes = Integer.parseInt(tokens[1]);

		Key key1 = new Key(release_year, no_of_votes);

		release_year = Integer.parseInt(tokens[2]);
		no_of_votes = Integer.parseInt(tokens[3]);

		Key key2 = new Key(release_year, no_of_votes);


		//Do search in leaves.
		Node leaf = tree.get_front_leaf();
		LinkedList<Integer> tids = new LinkedList<Integer>();

		System.out.print("Found pairs: [");
		while(leaf != null){
			for(int i = 0; i < leaf.dataCount; i++){
				if(leaf.data[i].largerEqualThan(key1) && leaf.data[i].smallerEqualThan(key2)){
					//print
					System.out.print("(");
					System.out.print(leaf.data[i]);
					System.out.print(",");

					//add all its tids
					System.out.print("[");
					for(int j =0; j < leaf.valueList[i].size(); j++){
						System.out.print(leaf.valueList[i].get(j));
						tids.addLast(leaf.valueList[i].get(j));
						if(j != leaf.valueList[i].size()-1){
							System.out.print(",");
						}
					}
					System.out.print("]");
					System.out.print(")");
					if(i != leaf.dataCount -1){
						System.out.print(",");
					}
				}
			}
			if(leaf.nextLeaf!= null && leaf.nextLeaf.dataCount >0 && leaf.data[leaf.dataCount-1].largerEqualThan(key1) && leaf.data[leaf.dataCount-1].smallerEqualThan(key2) && leaf.nextLeaf.data[0].largerEqualThan(key1) && leaf.nextLeaf.data[0].smallerEqualThan(key2)){
				System.out.print(",");
			}
			leaf = leaf.nextLeaf;
		}
		System.out.println("]");


		/* Find and print actual data */
		System.out.println("Attributes: <tid, movie, year of release, rating, no.of votes, director>");
		Movie movie;
		for(int k =0; k < tids.size(); k ++){
			int tid = (int) (tids.get(k));
			movie = movies.get(tid-1);
			System.out.print("Tuple #" + movie.tid + ":" + "<");
			System.out.print(movie.tid + ", ");
			System.out.print("\"" + movie.movie + "\", ");
			System.out.print(movie.year_of_release + ", ");
			System.out.print(movie.rating + ", ");
			System.out.print(movie.no_of_votes + ", ");
			System.out.print("\"" + movie.director + "\"");
			System.out.println(">");
		}
	}

	private static void print(Node tree){
		//BFS
		Queue<Node> q = new LinkedList<Node>();
		Node node;
		Node peek;

		tree.level = 1;
		q.add(tree);

		System.out.print("Level 1: ");
		while(!q.isEmpty()){
			node = q.remove();
			if(node.isLeaf()){

			}

			//Add all children.
			for(int i = 0; i < node.childCount; i++){
				node.subset[i].level = node.level+1;
				q.add(node.subset[i]);
			}

			//Print node.
			System.out.print(node);

			//Peak next element.
			peek = q.peek();
			if(peek == null){
				continue;
			}

			//Print Level information and commas.
			if(q.peek().level != node.level){	//means next one is new level.
				int new_level = node.level+1;
				System.out.print("\nLevel " +  new_level + ": ");
			}else if(node.isLeaf()){
				System.out.print(" --> ");
			}else{
				System.out.print(", ");
			}
			
		}
		System.out.println();
	}




	public static void printstuff(Key key,Node tree){
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

	private static void list_init(){
		String line;
		Movie movie;

		//Make scanner
		Scanner scanner = null;
		try{
		 scanner = new Scanner(new File("data.csv"));
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}

		//Load entire data into linked list just once
		if(movies.isEmpty()){
			scanner.nextLine();//get rid of titles.
			while(scanner.hasNextLine()){
				line = scanner.nextLine();
				String tokens[] = line.split(",");

				movie = new Movie(Integer.parseInt(tokens[0]), tokens[1], Integer.parseInt(tokens[2]), Float.parseFloat(tokens[3]), Integer.parseInt(tokens[4]),tokens[5]);
				movies.addLast(movie);
			}
			scanner.close();
		}
	}
}



		// node.add(5,1);
		// node.add(9,2);
		// node.add(1,3);
		// node.add(3,4);
		// node.add(4,5);
		// node.add(59,6);
		// node.add(65,7);
		// node.add(45,8);
		// node.add(89,9);
		// node.add(29,10);
		// node.add(68,11);
		// node.add(108,12);
		// node.add(165,14);
		// node.add(298,15);
		// node.add(219,16);
		// node.add(569,17);
		// node.add(37,18);
		// node.add(47,19);
		// node.add(1,69);//
		// node.add(1,70);
		// node.add(59,69);//
		// node.add(29,69);//

		// node.remove(5,1);
		// node.remove(9,2);
		// node.remove(1,3);
		// node.remove(1,69);//
		// node.remove(3,4);
		// node.remove(4,5);
		// node.remove(59,6);
		// node.remove(65,7);
		// node.remove(59,69);//
		// node.remove(45,8);
		// node.remove(89,9);
		// node.remove(29,10);
		// node.remove(68,11);
		// node.remove(108,12);
		// node.remove(165,14);
		// node.remove(298,15);
		// node.remove(219,16);
		// node.remove(569,17);
		// node.remove(37,18);
		// node.remove(47,19);


		// node.print(2);
		// System.out.println();
		// printstuff(1,node);