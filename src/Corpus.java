
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Corpus 
{
	/**
	 * Constructor
	 */
	public Corpus() { super(); }
	
	/**
	 * Cleaner
	 * @param inputFolder
	 * @param outputFolder
	 */
	public void cleaner(String inputFolder, String outputFolder)
	{
		// RECUPERATION DE TOUS LES FICHIES DU DOSSIER
		File[] folderFiles  = new File(inputFolder).listFiles();
	
		// RECUPERATION DES MOTS VIDES -- STOPWORDS
		ArrayList<String> stopwords  = new ArrayList<String>();
		this.loadDocWords("data/stopwords/stop-words-french.txt", stopwords);
		
		//LOOP FOR EACH DOCUMENT
		for(int i =0; i < folderFiles.length; i++)
		{
			// DOCUMENT LINES HOLDER
			ArrayList<String> documentLines  = new ArrayList<String>();
			
			//PROCESSING CURRENT DOCUMENT FILE
			this.processingLineCleaner(folderFiles[i].getPath(), documentLines, stopwords);
			
			// WRITE FILE CONTENT
			this.writeFile(documentLines, outputFolder+"/"+folderFiles[i].getName());			
		}	
		
		System.out.println("done !");
	}
	
	/**
	 * Processing line cleaner.
	 * @param documentPath
	 * @param documentLines
	 */
	private void processingLineCleaner(String documentPath, ArrayList<String> documentLines, ArrayList<String> stopwords)
	{
		Scanner scanner = null;
		try 
        {
            scanner = new Scanner(new File(documentPath));
            while (scanner.hasNextLine()) 
            {
    			// GETTING THE FIRST LINE
    			String line = scanner.nextLine();
    			
    			// CKECKS WHETHER THE LINE ISN'T EMPTY
    			if(!line.isEmpty())
    			{
    				// NETTOYAGE DU MOT
    				String wordChecker = line.replaceAll("[@|\\'|\\.|\\-|=|>|<|(|)|/|,]+", " ");
    				
    				// DECOUPLAGE IN CASE LE MOT EST DE LA FORME "j aime"
    				StringTokenizer st = new StringTokenizer(wordChecker);
    				
    				while(st.hasMoreElements())
    				{
    					String w = (String)st.nextElement();
    					
    					// VERIFIE SI LE MOT EST UN MOT VIDE (STOPWORDS)
        				if(!stopwords.contains(w.toLowerCase()))
        					documentLines.add(w.toLowerCase());
    				}
    			}
            }
        } 
        catch (FileNotFoundException e)  { e.printStackTrace(); }	
        finally  { scanner.close(); }		
	}
	
	/**
	 * Read stopwords.
	 * @return
	 */
	private ArrayList<String> loadDocWords(String filePathName)
	{
		ArrayList<String> stopwords = new ArrayList<String>();
		try
		{
			Scanner scanner = new Scanner(new File(filePathName));
			while(scanner.hasNextLine())
			{
				stopwords.add(scanner.nextLine().trim());
			}
			scanner.close();
		}
		catch(Exception e) { System.out.println(e.getMessage()); }
		
		return stopwords;
	}
	
	/**
	 * Processing document to keep only the stemmed word or the word if the stemme
	 * is unknown.
	 * @param inputFolder
	 * @param outputFolder
	 */
	public void stemmer(String inputFolder, String outputFolder)
	{
		// GET INPUT FOLDER FILES
		File[] folderFiles  = new File(inputFolder).listFiles();
	
		//LOOP FOR EACH DOCUMENT
		for(int i =0; i < folderFiles.length; i++)
		{
			// DOCUMENT LINES HOLDER
			ArrayList<String> documentLines  = new ArrayList<String>();
			
			//PROCESSING CURRENT DOCUMENT FILE
			this.processingLineStemmer(folderFiles[i].getPath(), documentLines);
			
			// WRITE FILE CONTENT
			this.writeFile(documentLines, outputFolder+"/"+folderFiles[i].getName());
		}		
		
		System.out.println("done !");
	}
	
	/**
	 * Processing line to keep only the stemmed word or the word if the stemme
	 * is unknown.
	 * @param documentPath
	 * @param documentLines
	 */
	private void processingLineStemmer(String documentPath, ArrayList<String> documentLines)
	{
		Scanner scanner = null;
		try 
        {
            scanner = new Scanner(new File(documentPath));
            while (scanner.hasNextLine()) 
            {
    			// GETTING THE FIRST LINE
    			String documentLine = scanner.nextLine();
    			
    			// CKECKS WHETHER THE LINE ISN'T EMPTY
    			if(!documentLine.isEmpty())
    			{
    				// PROVIDE LINE TOKENS
    				StringTokenizer tokens = new StringTokenizer(documentLine);
    				
    				// GETTING TOKENS
    				String word    = tokens.nextToken();
    				tokens.nextToken();
    				String stemme  = tokens.nextToken();
    				
    				// VERIFIE SI LE LEMMME N EST PAS INCONU
    				if(!stemme.equals("<unknown>")) documentLines.add(stemme);	
    				
    				// SINON RAJOUTER LE MOT
    				else documentLines.add(word);
    			}
            }
        } 
        catch (FileNotFoundException e) 
        	{ e.printStackTrace(); }	
        finally 
        	{ scanner.close(); }		
	}
	
	/**
	 * Pre-nétoyage du corpus de données.
	 * @param inputFolder
	 * @param outputFolder
	 */
	public void precleaner(String inputFolder, String outputFolder)
	{
		// GET INPUT FOLDER FILES
		File[] corpusFiles  = new File(inputFolder).listFiles();
		
		//LOOP FOR EACH DOCUMENT
		for(int i =0; i < corpusFiles.length; i++)
		{
			// DOCUMENT LINES HOLDER
			ArrayList<String> documentLines  = new ArrayList<String>();
			
			//PROCESSING CURRENT DOCUMENT FILE
			this.processingLinePreCleaner(corpusFiles[i].getPath(), documentLines);
			
			// WRITE FILE CONTENT
			this.writeFile(documentLines, outputFolder+"/"+corpusFiles[i].getName());
		}
		
		System.out.println("done !");
	}
	
	/**
	 * DOCUMENT READER METHOD.
	 * @param documentPath
	 * @param documentLineSets
	 */
	private void processingLinePreCleaner(String documentPath, ArrayList<String> documentLineSets)
	{
		Scanner scanner = null;
		try 
        {
            scanner = new Scanner(new File(documentPath));
            while (scanner.hasNextLine()) 
            {
    			// GETTING THE FIRST LINE
    			String documentLine = scanner.nextLine();
    			
    			// CKECKS WHETHER THE LINE ISN'T EMPTY
    			if(!documentLine.isEmpty())
    			{
    				// PROVIDE LINE TOKENS
    				StringTokenizer tokens = new StringTokenizer(documentLine);
    				
    				// GETTING TOKENS
    				String word    = tokens.nextToken();
    				String tag     = tokens.nextToken();
    				String stemme  = tokens.nextToken();
    				
					// FILTER
					if(tag.matches("(ADV)|(ADJ)|(NOM)|(VER:pper)|(VER:infi)"))
					{
						documentLineSets.add(word+" "+tag+" "+stemme);
					}
    			}
            }
        } 
        catch (FileNotFoundException e) 
        {
            e.printStackTrace();
        }	
        finally
        {
        	scanner.close();
        }
	}

	/**
	 * Load words from files.
	 * @param filePathName
	 * @param list
	 */
	private void loadDocWords(String filePathName, ArrayList<String> list)
	{
		try
		{
			Scanner scanner = new Scanner(new File(filePathName));
			while(scanner.hasNextLine())
			{
				String lineWord = scanner.nextLine().toLowerCase();
				
				if(!list.contains(lineWord))
				{
					list.add(lineWord);
				}
			}
			scanner.close();
		}
		catch(Exception e) { System.out.println(e.getMessage()); }
	}	
	
	/**
	 * CREATING ARFF BOOLEAN-T FILE 
	 * @param inputFolder
	 * @param outputFoler
	 * @throws Exception
	 */
	public void createBooleanFile(String inputFolder, String outputFoler) throws Exception
	{
		// RECUPERATION DE TOUS LES FICHIER DANS LE DOSSIER
		File[] corpusFiles  = new File(inputFolder).listFiles();
		
		// RECUPERATION DES NOMS DES CLASSES DANS LE CORPUS
		String classes = new String();
		for(int i = 0; i < corpusFiles.length; i ++)
		{
			String fileName = corpusFiles[i].getName();
			classes += fileName.substring(0, fileName.indexOf("."))+",";
		}
		classes = classes.substring(0, classes.lastIndexOf(","));
		
		// PREPARATION DE LA CREATION DU FICHIER
		PrintWriter writer = new PrintWriter(outputFoler+"/modelBoolean.arff");
		
		// ECRITURE DANS LE FICHIER OUTPUT
		System.out.println("@relation corpus");
		writer.println("@relation corpus");
		
		// RECUPERATION DE TOUS LES MOTS DU CORPUS
		ArrayList<String> corpusWords = new ArrayList<String>();
		for(int i =0; i < corpusFiles.length; i++)
		{
			// RECUPERATION DES MOTS DU CORPUS SANS DOUBLON
			this.loadDocWords(corpusFiles[i].getPath(), corpusWords);
		}
		
		// ECRITURE DE L'ENSEMBLE DES @TTRIBUTS DU CORPUS
		for(String word : corpusWords)
		{
			System.out.println("@attribute '"+ word +"' { t}");
			writer.println("@attribute '"+ word +"' { t}");
		}

		// ECRITURE DANS LE FICHIER -- TYPES DE CLASSES
		System.out.println("@attribute class {"+classes+"}");
		writer.println("@attribute class {"+classes+"}");
		
		// ECRITURE DANS LE FICHIER
		System.out.println("@data");
		writer.println("@data");
		
		// ECRITURE DES VALEURS BOOLEANS
		for(int i =0; i < corpusFiles.length; i++)
		{
			// RECUPERATION DES MOTS DE LA CLASSE
			ArrayList<String> currentDocumentWords = new ArrayList<String>();
			currentDocumentWords = loadDocWords(corpusFiles[i].getPath());
			
			// HOLDS UNE LIGNE A ECRIRE DANS LE FICHIER BOOLEAN
			ArrayList<String> currentDocumentLine = new ArrayList<String>();
			
			// AJOUT DES VALEURS BOOLEAN DANS LA LOGNE DE LA CLASSE COURANTE
			for(int j = 0; j < corpusWords.size(); j++)
			{
				String val = corpusWords.get(j);

				if(currentDocumentWords.contains(val))
					currentDocumentLine.add("1");
				else
					currentDocumentLine.add("?");
			}
			
			// RECONSTRUCTION DE LA CHAINE FINAL
			String stringLineData = new String();
			for (int k=0; k < currentDocumentLine.size(); k++)
			{
				stringLineData += currentDocumentLine.get(k)+",";
			}
			
			// CONCATENATION DE LA CLASSE D'APARTENANCE
			String suffix = corpusFiles[i].getName();
			stringLineData += suffix.substring(0, suffix.indexOf("."));
			
			//WRITTING LINE ON OUTPUT FILE
			System.out.println(stringLineData);
			writer.println(stringLineData);
		}
		
		//CLOSING BUFFER ELSE IT WONT WORK
		writer.close();
		
		System.out.println("done !");
	}

	/**
	 * Filter words by some threadshold
	 * @param inputFolder
	 * @param outputFolder
	 * @param seuil
	 */
	public void filter(String inputFolder, String outputFolder, int seuil)
	{
		// RECUPERATION DE TOUS LES FICHIERS DU DOSSIER
		File[] corpusFiles  = new File(inputFolder).listFiles();
		
		ArrayList<ArrayList<String>> ccs = new ArrayList<ArrayList<String>>();
		
		// PARCOURS POUR RECUPERER TOUS LES MOTS DU CORPUS
		for(int i =0; i < corpusFiles.length; i++)
		{
			// RECUPERATION DES MOTS DES CHAQUE CLASSE
			ArrayList<String> classeWords = new ArrayList<String>();
			classeWords = this.loadDocWords(corpusFiles[i].getPath());
			
			// AJOUT
			ccs.add(classeWords);
		}
		
		// PARCOURS DANS L'ENSAMBLE DES MOTS DU CORPUS POUR FILTRER
		for(int i = 0 ; i < corpusFiles.length; i ++)
		{
			ArrayList<String> nueva = new ArrayList<String>();
			
			for(int j = 0; j < ccs.get(i).size(); j++)
			{
				String word = ccs.get(i).get(j).toString();
				
				int gg = this.getGlobalFreq(ccs, word);
				
				if(gg >= seuil)
				{
					nueva.add(word);
				}
			}
			
			// ECRITURE DE LA NOUVELLE VERSION DU CORPUS
			this.writeFile(nueva, outputFolder+"/"+corpusFiles[i].getName());
		}
	}
	
	/**
	 * GETTING TERM FREQUENCY
	 * @param word
	 * @return
	 */
	private int getGlobalFreq(ArrayList<ArrayList<String>> ccs, String word)
	{
		int count = 0;
		
		for(int i =0; i< ccs.size();i++)
		{
			for(int j=0; j< ccs.get(i).size();j++)
			{
				if(word.equals(ccs.get(i).get(j).toString()))
				{	
					count ++;
				}
			}
		}
		
		return count;
	}
	
	/**
	 * Write content to file.
	 * @param fileLines
	 * @param filePathName
	 */
	private void writeFile(ArrayList<String> fileLines, String filePathName)
	{
		File file = new File(filePathName);
		FileWriter writer = null;
		try 
		{
			file.createNewFile();
			writer = new FileWriter(file);
		  
			// WRITE THE CONTENT TO THE FILE
			for(String line : fileLines)
			{
				if(!line.trim().isEmpty()) 
				{
				     writer.write(line+"\n"); 
				     writer.flush();
				}
			}
		} 
		catch (IOException e1) 
		{
			System.err.println("Error writing the new file...\n"+e1.getMessage());
		}
		finally 
		{ 
			try { writer.close(); } 
			catch (IOException e) { e.printStackTrace(); } 
		}
	}
	
	/**
	 * CREATION DU CORPUS
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception 
	{
		// CREATION DU COPUS
		new Corpus().precleaner("data/corpus-tag", "data/corpus-pre-clean");
		new Corpus().stemmer("data/corpus-pre-clean", "data/corpus-stremmer");
		new Corpus().cleaner("data/corpus-stremmer", "data/corpus-clean");
		new Corpus().filter("data/corpus-clean", "data/corpus-latest", 2);
		
		// CREATION DU FICHIER BOOLEAN
		new Corpus().createBooleanFile("data/corpus-latest", "data/corpus-vect");
	}
}