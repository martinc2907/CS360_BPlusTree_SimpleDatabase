import java.util.*;


public class Node{

	private final int MINIMUM = 1;
	private final int MAXIMUM = 2;

	//List of Keys owned.
	public int dataCount;
	public int[] data = new int[MAXIMUM+1];

	//List of children.
	public int childCount;
	public Node[] subset = new Node[MAXIMUM+2];

	//List of values.
	@SuppressWarnings("unchecked")
	public LinkedList<Integer>[] valueList = new LinkedList[MAXIMUM+1];

	//Pointer to next leaf.
	public Node nextLeaf;
	public Node prevLeaf;


	@SuppressWarnings("unchecked")
	public Node(){
		dataCount = 0;
		childCount = 0;
		nextLeaf = null;
		prevLeaf = null;
		for(int i = 0; i<MAXIMUM+1;i++){
			valueList[i] = new LinkedList<Integer>();
		}
	}

	public Node search(int key){
		if(isLeaf()){
			if(data[0] == key || data[1] == key){
				return this;
			}else{
				return null;
			}
		}else{

			// int index = firstGE(key);
			int index = searchIndex(key);
			return subset[index].search(key);
		}
	}


	private int searchIndex(int target){
		if(dataCount == 0){
			return 0;
		}
		if(dataCount == 1){
			if(target<data[0]){
				return 0;
			}else{
				return 1;
			}
		}
		if(dataCount == 2){
			if(target<data[0]){
				return 0;
			}
			else if(target < data[1]){
				return 1;
			}
			else{
				return 2;
			}
		}
		return 100000;
	}


	private int leafIndex(int target){
		// if(data[0] == target){
		// 	return 0;
		// }
		// else if(data[1] == target){
		// 	return 1;
		// }
		// else if(data[2] == target){
		// 	return 2;
		// }else{
		// 	return -1;
		// }
		int index=0;
		for(index = 0; index<dataCount; index++){
			if(data[index]>= target){
		  		return index;
			}
		}
		return index;    
	}


	private void insertData(int insertIndex, int entry)
   // Precondition: 0 <= insertIndex <= dataCount <= MAXIMUM.
   // Postcondition: The entry has been inserted at data[insertIndex] with
   // subsequent elements shifted right to make room. Also, dataCount has
   // been incremented by one.
   {
	  //consider datCount = 0 case and when insertIndex is bigger than any existing index.
	  if(dataCount == 0 || insertIndex == dataCount){
		  data[insertIndex] = entry;
	  }else{
	      for(int i = dataCount-1; i>=insertIndex; i--){//shift elements to right by one.
	    	  data[i+1] = data[i];
	    	  valueList[i+1].addAll(valueList[i]);
	    	  valueList[i].clear();
	      }
	      data[insertIndex]= entry;
	  }
      dataCount++;
   }


   private int deleteData(int removeIndex)
   // Precondition: 0 <= removeIndex < dataCount.
   // Postcondition: The element at data[removeIndex] has been removed and
   // subsequent elements shifted over to close the gap. Also, dataCount has
   // been decremented by one, and the return value is a copy of the
   // removed element.
   {
      // Student will replace this return statement with their own code:
	   
      int result = data[removeIndex];
      for(int i = removeIndex; i<dataCount-1; i++){
    	  data[i]=data[i+1];
    	  valueList[i].clear();
    	  valueList[i].addAll(valueList[i+1]);
      }
      dataCount--;
      return result;
   }


	private boolean isLeaf()
	// Return value is true if and only if the B-tree has only a root.
	{
		return (childCount == 0);
	}


	private boolean looseRemove(int key, int value){
	// Precondition:
	//   The entire B-tree is valid.
	// Postcondition:
	//   If target was in the set, then it has been removed from the set and the
	//   method returns true; otherwise the set is unchanged and the method 
	//   returns false. The entire B-tree is still valid EXCEPT that the
	//   number of entries in the root of this set might be one less than the
	//   allowed minimum.

		//leaf index returns index of element, or where is should be placed.
		int index = leafIndex(key);

		//Is leaf, not found here.
		if(isLeaf() && (index == dataCount || data[index] != key)){
			return false;
		}

		//Is leaf, found here.
		else if(isLeaf() && index < dataCount && data[index] == key){
			LinkedList<Integer> temp = valueList[index];
			temp.remove(value);
			if(temp.size()==0){
				deleteData(index);
			}
			return true;
		}

		//If inner node, recurse.
		else if(!isLeaf()){
			index = searchIndex(key);
			boolean answer = subset[index].looseRemove(key);
			if(subset[index].dataCount == 0){
				fixShortage(index);
			}
			return answer;
		}
		else{
			System.out.println("SHOULD NOT BE HERE??");
			return false;
		}

	}


	






















	private void looseAdd(int key, int value){
	// Precondition:
	//   The entire B-tree is valid.
	// Postcondition:
	//   If entry was already in the set, then the set is unchanged. Otherwise,
	//   entry has been added to the set, and the entire B-tree is still valid
	//   EXCEPT that the number of entries in the root of this set might be one
	//   more than the allowed maximum.

		//MUST add to leaf. It's the only way of adding.


		if(isLeaf()){	//Add to itself.
			int index = leafIndex(key);
			//Has key already 
			if(index != dataCount && dataCount != 0 && data[index] == key){
				valueList[index].add(value);
				return;
			}
			//Dont have key
			else{
				//add to node.
				insertData(index, key);
				valueList[index].add(value);
				return;
			}
		}

		else{	//is not leaf.
			int index = searchIndex(key);
			subset[index].looseAdd(key,value);
			if(subset[index].dataCount > MAXIMUM){
				fixExcess(index);
			}
		}
	}

	private void insertSubset(int insertIndex, Node set)
   // Precondition: 0 <= insertIndex <= childCount <= MAXIMUM+1.
   // Postcondition: The set has been inserted at subset[insertIndex] with
   // subsequent elements shifted right to make room. Also, childCount has
   // been incremented by one.
   {
	   if(childCount == 0 || insertIndex == childCount ){
		   subset[insertIndex] = set;
	   }else{
		   for(int i = childCount-1; i>=insertIndex; i--){
			   subset[i+1]=subset[i];
		   }
		   subset[insertIndex] = set;
	   }
	   childCount++;
   }

	private void fixExcess(int i){
		// Precondition: 
		//   (i < childCount) and the entire B-tree is valid EXCEPT that
		//   subset[i] has MAXIMUM + 1 entries. Also, the root is allowed to have
		//   zero entries and one child.
		// Postcondition: 
		//   The tree has been rearranged so that the entire B-tree is valid EXCEPT
		//   that the number of entries in the root of this set might be one more than
		//   the allowed maximum.


		//Depends on whether the node being fixed is leaf or not.

		Node fixee = subset[i];

		//if leaf.
		if(fixee.isLeaf()){
			int middleElement = subset[i].data[1];

			Node left = subset[i];
			left.dataCount = 1;
			left.childCount = 0;

			Node right = new Node();
			right.dataCount = 2;
			right.childCount = 0;


			//update before making new link.- this is because we are leaving left as the original node.
			right.nextLeaf = left.nextLeaf;

			left.nextLeaf = right;
			right.prevLeaf = left;

			//Copy to right.
			right.data[0] = left.data[1];
			right.data[1] = left.data[2];

			//Update valueLists.
			right.valueList[0].addAll(left.valueList[1]);
			right.valueList[1].addAll(left.valueList[2]);
			left.valueList[1].clear();
			left.valueList[2].clear();

			insertSubset(i+1, right);
			insertData(i,middleElement);
		}

		//not leaf- follow standard b tree algo.
		else{
			int middleElement = subset[i].data[1];

			Node left = subset[i];
			left.dataCount = 1;
			if(!left.isLeaf()){
				left.childCount = left.dataCount+1;
			}

			Node right = new Node();
			right.dataCount = 1;
			if(!left.isLeaf()){
				right.childCount = 2;
			}

			//copy to right.
			right.data[0] = left.data[2];
			right.subset[0] = left.subset[2];
			right.subset[1] = left.subset[3];

			//insert new split childs to root. Just insert right at i+1.
			insertSubset(i+1, right);

			insertData(i,middleElement);
		}
	}


	public void add(int key, int value){
		looseAdd(key,value);

		if(dataCount > MAXIMUM){
			Node newRoot = new Node();
			newRoot.data = data.clone();
			newRoot.subset = subset.clone();
			newRoot.childCount = childCount;
			newRoot.dataCount = dataCount;

			//no need to do this for leaf nodes only. If not leaf node, copies null.
			newRoot.valueList[0].addAll(valueList[0]);
			newRoot.valueList[1].addAll(valueList[1]);
			newRoot.valueList[2].addAll(valueList[2]);
			valueList[0].clear(); 
			valueList[1].clear(); 
			valueList[2].clear();

			dataCount = 0;
			childCount = 0;

			insertSubset(0, newRoot);
			fixExcess(0);
		}
	}


	public void print(int indent)
   // Print a representation of this set's B-tree, useful during debugging.
   {
      final int EXTRA_INDENTATION = 4;
      int i;
      int space;
  
      // Print the indentation and the data from this node
      for (space = 0; space < indent; space++)
         System.out.print(" ");
      for (i = 0; i < dataCount; i++)
         System.out.print(data[i] + " ");
      System.out.println( );
         
      // Print the subtrees
      for (i = 0; i < childCount; i++)
         subset[i].print(indent + EXTRA_INDENTATION);
   }




}