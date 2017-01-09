import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Driver {

	public static void main(String[] args) {
		KeyScheduler keyScheduler;
		String inputText,plainText,inputKeyText,key, cipherText ;
		String[] inputTextArray, inputKeyArray;
		Converter converter = new Converter();
		RC6 rc6;
		try {
			FileInputStream fileInputStream = new FileInputStream(args[0]);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
			FileWriter outputFile = new FileWriter(args[1]);
			BufferedWriter outputWriter = new BufferedWriter(outputFile);
			String mode = bufferedReader.readLine();
			
			String encryptionMode = "Encryption";
			String DecrytionMode = "Decryption";
			if(!(mode.equals(encryptionMode) || mode.equals(DecrytionMode))){
				System.out.println("File contents are invalid");
				System.exit(0);
			}
			if(mode.equals(encryptionMode)){
				inputText = bufferedReader.readLine();
				inputTextArray = inputText.split(":");
				plainText = inputTextArray[1];
				plainText = plainText.replace(" ","");
				inputKeyText = bufferedReader.readLine();
				inputKeyArray = inputKeyText.split(":");
				key = inputKeyArray[1];
				key = key.replace(" ","");
				//System.out.println(key);
				keyScheduler = KeyScheduler.getInstance(key);
				RC6.S = keyScheduler.generateSubKey();
				/*System.out.println("Displaing S array in driver...");
				for(int i = 0;i<RC6.S.length;i++){
					System.out.print(RC6.S[i]+" ");
				}*/
				System.out.println();
				byte[] textDataInByte = converter.StringToByteArray(plainText);
				//System.out.println("Plain text in byte is: "+textDataInByte);
				rc6 = new RC6();
				byte[] encryptedDataInBytes = rc6.encrypt(textDataInByte); 
				String encryptedData = converter.byteToHex(encryptedDataInBytes);
				encryptedData = encryptedData.replaceAll("..(?!$)", "$0 ");
				//System.out.println("Writing encrypted data");
				//System.out.println(encryptedData);
				outputWriter.write("ciphertext: "+encryptedData);
				outputWriter.close();
			}
			
			if(mode.equals(DecrytionMode)){
				inputText = bufferedReader.readLine();
				inputTextArray = inputText.split(":");
				cipherText = inputTextArray[1];
				cipherText = cipherText.replace(" ","");
				inputKeyText = bufferedReader.readLine();
				inputKeyArray = inputKeyText.split(":");
				key = inputKeyArray[1];
				key = key.replace(" ","");
				keyScheduler = KeyScheduler.getInstance(key);
				RC6.S = keyScheduler.generateSubKey();
				byte[] cipherDataInByte = converter.StringToByteArray(cipherText);
				rc6 = new RC6();
				byte[] decryptedDataInBytes = rc6.decrypt(cipherDataInByte);
				String decryptedData = converter.byteToHex(decryptedDataInBytes);
				decryptedData = decryptedData.replaceAll("..(?!$)", "$0 ");
				outputWriter.write("plaintext: "+decryptedData);
				outputWriter.close();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(IOException e){
			e.printStackTrace();
		}
		
		

	}

}
