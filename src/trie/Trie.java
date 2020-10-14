package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie.
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {

	// prevent instantiation
	private Trie() {
	}

	/**
	 * Builds a trie by inserting all words in the input array, one at a time, in
	 * sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!) The words in the
	 * input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */

	// to traverse tree and insert newword
	public static TrieNode traverseTrie(TrieNode node, String newword, String[] allWords, int index) {// traverse
																										// through the
																										// tree
		// base case
		if (node == null) {
			return null;
		}

		// substring: compares entire substring, if == go to firstChild
		if((newword.length()-1)>=node.substr.endIndex) {
		if (newword.substring((int) node.substr.startIndex, ((int) node.substr.endIndex + 1)).equals(allWords[node.substr.wordIndex].substring(node.substr.startIndex, (node.substr.endIndex + 1)))) {
			return traverseTrie(node.firstChild, newword, allWords, index);} }
		// word:checks to see if they share a common root
		if (newword.charAt(node.substr.startIndex) == allWords[node.substr.wordIndex].charAt(node.substr.startIndex)) {
			
			// x is the first thing that isnt the same
			int x = compare(newword, node, allWords); // x is the last same letter
			//ptr node to check to see if og has children
			TrieNode ptrnode = node.firstChild;
			// hold info from prefix node, new child node
			Indexes oldSubstr = new Indexes(node.substr.wordIndex, (short) (x + 1), (short) node.substr.endIndex); // CHECK***
			// new longer prefix
			Indexes prefSubstr = new Indexes(node.substr.wordIndex, (short) node.substr.startIndex, (short) (x));
			// new word addition, new child.sibling
			Indexes newSubstr = new Indexes(index, (short) (x + 1), (short) (newword.length() - 1));
			// change prefix node
			node.substr = prefSubstr;
			// new child node
			node.firstChild = new TrieNode(oldSubstr, null, null);
			//checks to see if originalnode has children
			if(ptrnode!=null) {
			node.firstChild.firstChild = ptrnode;}
			// new sibling node
			node.firstChild.sibling = new TrieNode(newSubstr, null, null);

			return node.firstChild.sibling;
		}

		// if there are no more children to go to THEN add newword as a TrieNode
		else if (node.sibling == null) {
			Indexes subbo = new Indexes(index, (short) 0, (short) (newword.length() - 1));
			node.sibling = new TrieNode(subbo, null, null);

			// ******
			return node.sibling;
		}

		// No result
		return traverseTrie(node.sibling, newword, allWords, index);
	}
	

	public static int compare(String newword, TrieNode node, String[] thewords) {

		int i = node.substr.startIndex;

		if (node.substr.startIndex != node.substr.endIndex) {// if that then return i-1
			// insert if statement to check if a character is empty
			while (newword.charAt(i) == thewords[node.substr.wordIndex].charAt(i)) {
				if (newword.substring(i + 1, i + 2).isEmpty()) { // next letter does not exist
					break;
				}
				i++;
			}
			return i - 1;
		}

		return i;
		// Indexes substr = new Indexes(index, (short) i, (short) (newWord.length()-1));

	}

	public static TrieNode buildTrie(String[] allWords) {
		/** COMPLETE THIS METHOD **/
		// creates the Tree
		TrieNode root = new TrieNode(null, null, null);

		// fills the tree
		Indexes newIndex = new Indexes(0, (short) 0, (short) (allWords[0].length() - 1));
		TrieNode firstNode = new TrieNode(newIndex, null, null);
		// creates first child
		root.firstChild = firstNode;

		for (int i = 1; i < allWords.length; i++) {// traverses the dictionary
			traverseTrie(root.firstChild, allWords[i], allWords, i);// inserts every word
			}

		// loop through and print the whole tree
		return root;

	}

	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf
	 * nodes in the trie whose words start with this prefix. For instance, if the
	 * trie had the words "bear", "bull", "stock", and "bell", the completion list
	 * for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell";
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and
	 * "bell", and for prefix "bell", completion would be the leaf node that holds
	 * "bell". (The last example shows that an input prefix can be an entire word.)
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be", the
	 * returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root     Root of Trie that stores all words to search on for
	 *                 completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix   Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the
	 *         prefix, order of leaf nodes does not matter. If there is no word in
	 *         the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		//essentials
		TrieNode ptr = root.firstChild;
		ArrayList<TrieNode> storage = new ArrayList<TrieNode>();

		//keeps length of prefix
		int min= prefix.length();
		//where the magic happens
		return finda(ptr, storage, allWords, min, prefix);
	}
	
private static ArrayList<TrieNode> finda(TrieNode ptr, ArrayList<TrieNode> storagee, String[]allWords, int l, String prefix){
		
	//for loop to traverse thru 
		while(ptr != null) {
			String word=allWords[ptr.substr.wordIndex].substring(0, ptr.substr.endIndex + 1);
			//checker thing we did in recitation with rishab or sumtin
			int mismo = commonPrefix(word, prefix);
			
			if(mismo == -1) {
				ptr=ptr.sibling;}
			else{
				if(mismo < l) {
					if(ptr.firstChild != null) {
						storagee = finda(ptr.firstChild, storagee, allWords, l, prefix);
						if(storagee==null) {
							storagee = new ArrayList<TrieNode>();}
						ptr = ptr.sibling;}
					else {ptr = ptr.sibling;}}
				else {
					if(ptr.firstChild != null) {
						storagee = finda(ptr.firstChild, storagee, allWords, l, prefix);
						ptr=ptr.sibling;
						if(storagee==null) {
							storagee = new ArrayList<TrieNode>();
						}}
					else {
						storagee.add(ptr);
						ptr = ptr.sibling;
					}}}}
		//v help me
		if (storagee.isEmpty() == true||storagee==null ) {
			return null;}
		return storagee;
	}


private static int commonPrefix(String a, String b) {
	//helper method pof
	int same = 0;
	int min = Math.min(a.length(), b.length());
	//shiii took too long
	for (int i = 0; i < min; i++) {
		if (b.charAt(0) != a.charAt(0)) {
			return -1;}
		if (b.charAt(i) == a.charAt(i)) {
			same++;}
		else {
			return same;
		}}
	return same;
}


	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
		print(root, 1, allWords);
	}

	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}

		if (root.substr != null) {
			String pre = words[root.substr.wordIndex].substring(0, root.substr.endIndex + 1);
			System.out.println("      " + pre);
		}

		for (int i = 0; i < indent - 1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}

		for (TrieNode ptr = root.firstChild; ptr != null; ptr = ptr.sibling) {
			for (int i = 0; i < indent - 1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent + 1, words);
		}
	}
}
