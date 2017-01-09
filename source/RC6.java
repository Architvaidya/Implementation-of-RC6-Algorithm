import java.util.ArrayList;
import java.util.List;

public class RC6 {
	
	public static final int P = 0xb7e15163;
	public static final int Q = 0x9e3779b9;
	public static final int r = 20;
	public static final int w = 32;
	public static int[] S;
	
	
	public byte[] encrypt(byte[] input){
		
		Converter converter = new Converter();
		
		
		
		int tmp, t, u;
		
		List<Integer> tempOutput = new ArrayList<Integer>();
		byte[] output = new byte[input.length];
		
		for(int i = 0; i < (input.length/4);i++){
			tempOutput.add(0);
		}
		
		//System.out.println("The length that is sent from encryption block is: "+tempOutput.size());
		//tempOutput = converter.bytesToInt(input, tempOutput.size()/4); 
		tempOutput = converter.bytesToInt(input, tempOutput.size());
		//System.out.println(tempOutput.size());
		//System.out.println("Temp output after conversion: "+tempOutput);
		
		int A,B,C,D = 0;
		
		A = tempOutput.get(0);
		B = tempOutput.get(1);
		C = tempOutput.get(2);
		D = tempOutput.get(3);
		
		B = B + S[0];
		D = D + S[1];
		
		/*System.out.println("Printing S array in encryption: ");
		for(int i = 0;i<S.length;i++){
			System.out.print(" "+S[i]);
		}
		System.out.println();*/
		
		int logw = (int) (Math.log(RC6.w)/Math.log(2));
		
		
		for(int i = 1; i <=r; i++ ){
			t = Integer.rotateLeft(B*(2*B + 1), logw);
			u = Integer.rotateLeft(D*(2*D+1),logw);
			//System.out.println("Iteration:"+i+" u:"+u);
			A = Integer.rotateLeft((A^t), u) + S[2*i];
			C = Integer.rotateLeft((C^u),t) + S[2*i+1];
			
			tmp = A;
			A = B;
			B = C;
			C = D;
			D = tmp;
			//System.out.println("Iteration "+i+" t: "+t+" u: "+u+" A: "+A+" C: "+C);
		}
		
		A = A + S[2*r+2];
		C = C + S[2*r+3];
		//System.out.println(A+" "+B+" "+C+" "+D);
		tempOutput.add(0, A);
		tempOutput.add(1, B);
		tempOutput.add(2, C);
		tempOutput.add(3, D);
		
		//System.out.println("Printing output array in int...");
		//System.out.println(tempOutput.size());
		//System.out.println(tempOutput);
		
		output = converter.intToByte(tempOutput, input.length);
		
		return output;
		
		
		
	}
	
	public byte[] decrypt(byte[] input){
		
		int tmp, t, u;
		
		Converter converter = new Converter();
		
		List<Integer> tempOutput = new ArrayList<Integer>(input.length/4);
		
		
		for(int i = 0; i<(input.length/4);i++){
			tempOutput.add(i);
		}
		
		//System.out.println("Length sent to bytesToInt is: "+tempOutput.size());
		tempOutput = converter.bytesToInt(input, tempOutput.size());
		
		int A, B, C, D;
		A=tempOutput.get(0);
		B=tempOutput.get(1);
		C=tempOutput.get(2);
		D=tempOutput.get(3);
		
		C = C - S[2*RC6.r+3];
		A = A - S[2*RC6.r+2];
		//System.out.println("Inside Decryption, A:"+A+" B:"+B+" C:"+C+" D: "+D);
		/*System.out.println("Printing S array in decryption mode...");
		for(int i = 0; i< S.length;i++){
			System.out.print(S[i]+" ");
		}
		System.out.println();*/
		
		int logw = (int) (Math.log(RC6.w)/Math.log(2));
		
		byte[] output = new byte[input.length];
		//System.out.println("Size of S array is: "+S.length);
		for(int i = RC6.r; i >= 1; i-- ){
			tmp = D;
			  D = C;
			  C = B;
			  B = A;
			  A = tmp;
			
			u = Integer.rotateLeft(D*(2*D+1), logw);
			t = Integer.rotateLeft(B*(2*B+1), logw);
			C = (Integer.rotateRight(C-S[2*i+1], t))^u;
			A = (Integer.rotateRight(A - S[2*i], u))^t;
			//System.out.println("Iteration i:"+i+" u:"+u+" t:"+t );
			
		}
		D=D-S[1];
		B=B-S[0];
		
		tempOutput.add(0, A);
		tempOutput.add(1, B);
		tempOutput.add(2, C);
		tempOutput.add(3, D);

		output = converter.intToByte(tempOutput,input.length);
		return output;
		
		
	}

}
