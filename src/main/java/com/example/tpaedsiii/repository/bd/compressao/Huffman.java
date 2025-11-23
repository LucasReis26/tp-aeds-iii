package com.example.tpaedsiii.repository.bd.compressao;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;

public class Huffman {
	private String orgStr, encodedStr, decodedStr;
	public HashMap<Character, Integer> hmapWC;  // for occurrence count
	public HashMap<Character, String> hmapCode; // for code(character/code)
	public HashMap<String, Character> hmapCodeR; // for code(code/character)
	private PriorityQueue<node> pq;  // for MinHeap
	private int counter;  // Unique id assigned to each node
	private int treeSize;  // # of total nodes in the tree
	private node root;

	// Inner class
	private class node{
		int uid, weight;
		char ch;
		node left, right;
		
		// Constructor for class node
		private node(Character ch, Integer weight, node left, node right){
			uid = ++counter;
			this.weight = weight;
			this.ch = ch;
			this.left = left;
			this.right = right;
		}	
	}
	
	// Constructor for class Huffman
	public Huffman(String orgStr, boolean show, String dotfilename){
		this.counter = 0;
		this.treeSize = 0;
		this.orgStr = orgStr;
		hmapWC = new HashMap<Character, Integer>();
		hmapCode = new HashMap<Character, String>();
		hmapCodeR = new HashMap<String, Character>();
		pq = new PriorityQueue<node>(1, new Comparator<node>() {
	        @Override
	        public int compare(node n1, node n2) {
	        	if (n1.weight < n2.weight)
	        		return -1;
	        	else if (n1.weight > n2.weight)
	        		return 1;
	        	return 0;
	        }
	    });
		
		countWord();  // STEP 1: Count frequency of word
		buildTree();  // STEP 2: Build Huffman Tree
		writeDot(dotfilename);  // STEP 3: Write .dot file to visualize the tree with Graphviz software
		buildCodeTable();  // STEP 4: Build Huffman Code Table
	}
		
	private void buildCodeTable(){
		String code = "";
		node n = root;
		buildCodeRecursion(n, code);  // Recursion
	}
	
	private void buildCodeRecursion(node n, String code){
		if (n != null){
			if (! isLeaf(n)){  // n = internal node
				buildCodeRecursion(n.left, code + '0');
				buildCodeRecursion(n.right, code + '1');
			}
			else{  // n = Leaf node
				hmapCode.put(n.ch, code); // for {character:code}
				hmapCodeR.put(code, n.ch); // for {code:character}
			}
		}
	}
	
	private void writeDot(String fname){
		if (treeSize > 1){
			node n = root;
			try (PrintWriter o = new PrintWriter(new BufferedWriter (new FileWriter(fname)))){
				o.println("## Command to generate pdf:  dot -Tpdf test.dot -o test.pdf");
				o.println("digraph g {");
				dotWriteRecursion(n, o);  // Recursion
				o.println("}");
			}
			catch (IOException e){
				System.out.println(e);
			}
		}
	}
	
	private void dotWriteRecursion(node n, PrintWriter o){
		if (! isLeaf(n)){
			if (n.left != null){  // has left kid
				String t = "";
				char c = n.left.ch;
				if (c != '\0' && c != ' ' && c != '"' && c!= '\n')  // regular characters
					t = "\\n " + c; 
				else if (c == ' ') 
					t = "\\n blank";
				else if (c == '"')  //escape "
					t = "\\n \\\"";
				else if (c == '\n') 
					t = "\\n /n";
				o.println(" \"" + n.uid + "\\n" + n.weight + "\" -> \"" + n.left.uid + "\\n" + n.left.weight + t + "\" [color=red, label=0]");
				dotWriteRecursion(n.left, o);
			}
			if (n.right != null){ // has right kid
				String t = "";
				char c = n.right.ch;	
				if (c != '\0' && c != ' ' && c != '"' && c != '\n') // regular characters
					t = "\\n " + c;
				else if (c == ' ')
					t = "\\n blank"; 
				else if (c == '"')  //escape
					t = "\\n \\\"";
				else if (c == '\n')
					t = "\\n /n";
				o.println(" \"" + n.uid + "\\" +"n" + n.weight + "\" -> \"" + n.right.uid + "\\n" + n.right.weight + t + "\" [color=blue, label=1]");
				dotWriteRecursion(n.right, o);
			}
		}
	}
		
	private void buildTree(){
		buildMinHeap();  // Set all leaf nodes into MinHeap
		node left, right;
		while (! pq.isEmpty()){
			left = pq.poll(); treeSize++;
			if (pq.peek() != null){
				right = pq.poll();  treeSize++;
				root = new node('\0', left.weight + right.weight, left, right);
			}
			else{  // only left child. right=null
				root = new node('\0', left.weight, left, null);
			}
			
			if (pq.peek() != null){
				pq.offer(root);
			}
			else{  // = Top root. Finished building the tree.
				treeSize++;
				break;
			}
		}
	}
	
	private void buildMinHeap(){
		for (Map.Entry<Character, Integer> entry: hmapWC.entrySet()){
			Character ch = entry.getKey();
	        Integer weight = entry.getValue();
	        node n = new node(ch, weight, null, null);
	        pq.offer(n);
		}		
	}
	
	private void countWord(){
		Character ch;
		Integer weight;
		for (int i=0; i<orgStr.length(); i++){
			ch = new Character(orgStr.charAt(i));
			if (hmapWC.containsKey(ch) == false)
				weight = new Integer(1);
			else
				weight = hmapWC.get(ch) + 1;
			hmapWC.put(ch, weight);
		}
	}
	
	private boolean isLeaf(node n) {
        return (n.left == null) && (n.right == null);
    }
	
	public String encode(){
		StringBuilder sb = new StringBuilder();
		Character ch;
		for(int i=0; i<orgStr.length(); i++){
			ch = orgStr.charAt(i);
			sb.append(hmapCode.get(ch));
		}
		encodedStr = sb.toString();
		return encodedStr;
	}
	
	public String decode(){
		StringBuilder sb = new StringBuilder();
		String t = "";
		
		for(int i=0; i<encodedStr.length(); i++){
			t += encodedStr.charAt(i);
			if (hmapCodeR.containsKey(t)){
				sb.append(hmapCodeR.get(t));
				t = "";
			}
		}
		decodedStr = sb.toString();
		return decodedStr;
	}
	
	// Métodos estáticos para compressão de bytes
	public static byte[] comprimirBytes(byte[] dados) throws Exception {
		if (dados == null || dados.length == 0) {
			return new byte[0];
		}
		
		// Converte bytes para String
		String texto = new String(dados, "ISO-8859-1");
		
		// Cria o objeto Huffman e comprime
		Huffman huffman = new Huffman(texto, false, "");
		String encodedStr = huffman.encode();
		
		// Serializa a tabela de códigos e os dados comprimidos
		java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
		java.io.DataOutputStream dos = new java.io.DataOutputStream(baos);
		
		// Salva a tabela de códigos de forma mais eficiente
		dos.writeShort(huffman.hmapCode.size()); // 2 bytes ao invés de 4
		for (Map.Entry<Character, String> entry : huffman.hmapCode.entrySet()) {
			dos.writeChar(entry.getKey()); // 2 bytes
			dos.writeByte(entry.getValue().length()); // 1 byte para tamanho do código
			// Converte string de código em bits compactos
			String codigo = entry.getValue();
			int numBytesCodigo = (codigo.length() + 7) / 8;
			byte[] bytesCodigo = new byte[numBytesCodigo];
			for (int i = 0; i < codigo.length(); i++) {
				if (codigo.charAt(i) == '1') {
					bytesCodigo[i / 8] |= (1 << (7 - (i % 8)));
				}
			}
			dos.write(bytesCodigo);
		}
		
		// Converte a string de bits para bytes
		int numBits = encodedStr.length();
		dos.writeInt(numBits); // 4 bytes
		
		// Converte string de bits em bytes
		int numBytes = (numBits + 7) / 8;
		byte[] bitsComprimidos = new byte[numBytes];
		for (int i = 0; i < numBits; i++) {
			if (encodedStr.charAt(i) == '1') {
				bitsComprimidos[i / 8] |= (1 << (7 - (i % 8)));
			}
		}
		
		dos.write(bitsComprimidos);
		
		dos.close();
		return baos.toByteArray();
	}
	
	public static byte[] descomprimirBytes(byte[] dadosComprimidos) throws Exception {
		if (dadosComprimidos == null || dadosComprimidos.length == 0) {
			return new byte[0];
		}
		
		java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(dadosComprimidos);
		java.io.DataInputStream dis = new java.io.DataInputStream(bais);
		
		// Lê a tabela de códigos
		int tamanhoTabela = dis.readShort(); // 2 bytes
		HashMap<String, Character> hmapCodeR = new HashMap<>();
		for (int i = 0; i < tamanhoTabela; i++) {
			char ch = dis.readChar(); // 2 bytes
			int tamanhoCode = dis.readByte() & 0xFF; // 1 byte (unsigned)
			
			// Lê os bytes do código
			int numBytesCodigo = (tamanhoCode + 7) / 8;
			byte[] bytesCodigo = new byte[numBytesCodigo];
			dis.read(bytesCodigo);
			
			// Reconstrói a string do código
			StringBuilder codigo = new StringBuilder();
			for (int j = 0; j < tamanhoCode; j++) {
				int byteIndex = j / 8;
				int bitIndex = 7 - (j % 8);
				if ((bytesCodigo[byteIndex] & (1 << bitIndex)) != 0) {
					codigo.append('1');
				} else {
					codigo.append('0');
				}
			}
			
			hmapCodeR.put(codigo.toString(), ch);
		}
		
		// Lê os bits comprimidos
		int numBits = dis.readInt(); // 4 bytes
		int numBytes = (numBits + 7) / 8;
		byte[] bitsComprimidos = new byte[numBytes];
		dis.read(bitsComprimidos);
		
		// Reconstrói a string de bits
		StringBuilder encodedStr = new StringBuilder();
		for (int i = 0; i < numBits; i++) {
			int byteIndex = i / 8;
			int bitIndex = 7 - (i % 8);
			if ((bitsComprimidos[byteIndex] & (1 << bitIndex)) != 0) {
				encodedStr.append('1');
			} else {
				encodedStr.append('0');
			}
		}
		
		// Decodifica usando a tabela
		StringBuilder sb = new StringBuilder();
		String t = "";
		for (int i = 0; i < encodedStr.length(); i++) {
			t += encodedStr.charAt(i);
			if (hmapCodeR.containsKey(t)) {
				sb.append(hmapCodeR.get(t));
				t = "";
			}
		}
		
		dis.close();
		return sb.toString().getBytes("ISO-8859-1");
	}
	
}