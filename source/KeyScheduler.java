

public class KeyScheduler {
	
	private static KeyScheduler keyScheduler;
	public String key;
	public byte[] keyInByte;
	
	private KeyScheduler(String keyIn){
		key = keyIn;
	}
	
	public static KeyScheduler getInstance(String key){
		if(keyScheduler == null){
			keyScheduler = new KeyScheduler(key);
		}
		return keyScheduler;
	}
	
	public void processkey(){
		Converter converter = new Converter();
		keyInByte = converter.StringToByteArray(key);
		//System.out.println("Key in byte is: "+keyInByte);
	}
	
	
	
	public int[] generateSubKey(){
		
		Converter converter = new Converter();
		processkey();
		//System.out.println();
		RC6.S = new int[2*(RC6.r) + 4];
		RC6.S[0] = RC6.P;
		//System.out.println("S[0] is: "+RC6.S[0]);
		int c = keyInByte.length / ((RC6.w)/8);
		
		int[] L = converter.bytestoWords(keyInByte, c);
		
		/*System.out.println("Printing L array: ");
		for(int i = 0;i < L.length;i++){
			System.out.print(L[i]+" ");
		}
		System.out.println();*/
		for (int i = 1; i < ((2 * RC6.r) + 4); i++){
			RC6.S[i] = RC6.S[i - 1] + RC6.Q;
		}
		
		/*System.out.println("S array afrer initializing is: ");
		for(int i = 0; i<RC6.S.length;i++){
			System.out.print(RC6.S[i]+" ");
		}*/
		
		int A,B,i,j;
		
		A=B=i=j=0;

		int v = 3 * Math.max(c, (2 * RC6.r + 4));
		//System.out.println("Loading S array: ");
		for (int s = 1; s <= v; s++) {
			//System.out.println(A+" "+B+" ");
			A = RC6.S[i] = Integer.rotateLeft(RC6.S[i] + A + B, 3);
			
			//System.out.print(A+" ");
			B = L[j] = Integer.rotateLeft((L[j] + A + B), (A+B));
			
			i = (i + 1) % (2 * (RC6.r) + 4);
			j = (j + 1) % c;
			//System.out.print("For i-> "+i+" "+RC6.S[i]+" ");

		}
		
		//System.out.println("Last is: "+RC6.S[RC6.S.length-1]);
		/*System.out.println("Printing S array in key scheduler: ");
		for(i = 0;i<RC6.S.length;i++){
			System.out.print(RC6.S[i]+" ");
		}*/
		//System.out.println();

		return RC6.S;
		
	}
	

}
