package naive.bayes.classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.nio.file.*;


public class Train {
    static int total_ham_words;
    static int total_spam_words;
    static int spam_files;
    static int ham_files;
    static int total_files;
    static List<Integer>labels = new ArrayList();
    static String[] stop_word = new String[]{
        "a", "about", "above", "across", "after", "afterwards", 
        "again", "all", "almost", "alone", "along", "already", "also",    
        "although", "always", "am", "among", "amongst", "amoungst", "amount", 
        "an", "and", "another", "any", "anyhow", "anyone", "anything", "anyway", "anywhere", 
        "are", "as", "at", "be", "became", "because", "become","becomes", "becoming", 
        "been", "before", "behind", "being", "beside", "besides", "between", "beyond", 
        "both", "but", "by","can", "cannot", "cant", "could", "couldnt", "de", "describe", 
        "do", "done", "each", "eg", "either", "else", "enough", "etc", "even", "ever", "every",
        "everyone", "everything", "everywhere", "except", "few", "find","for","found", "four", "from",
        "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", 
        "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", 
        "however", "i", "ie", "if", "in", "indeed", "is", "it", "its", "itself", "keep", "least", "less", "ltd",
        "made", "many", "may", "me", "meanwhile", "might", "mine", "more", "moreover", "most", "mostly", "much", 
        "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next","no", "nobody",
        "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", 
        "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own", 
        "part","perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", 
        "she", "should","since", "sincere","so", "some", "somehow", "someone", "something", "sometime", "sometimes", 
        "somewhere", "still", "such", "take","than", "that", "the", "their", "them", "themselves", "then", "thence", 
        "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they",
        "this", "those", "though", "through", "throughout",
        "thru", "thus", "to", "together", "too", "toward", "towards",
        "under", "until", "up", "upon", "us",
        "very", "was", "we", "well", "were", "what", "whatever", "when",
        "whence", "whenever", "where", "whereafter", "whereas", "whereby",
        "wherein", "whereupon", "wherever", "whether", "which", "while", 
        "who", "whoever", "whom", "whose", "why", "will", "with",
        "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves"
    };
    
    public static void main(String[] args) {
        total_ham_words = 0;
        total_spam_words = 0;
        String result;
        int count = 0;
        int count_files = 0;
         List<wordFrequency> dictionary = new ArrayList<wordFrequency>();
         dictionary = learn_ham(dictionary);
         dictionary = learn_spam(dictionary);
         train_LR(dictionary);
         Train.total_files = Train.ham_files + Train.spam_files;
         for(wordFrequency i:dictionary){
             System.out.println(i.word() + "\t" + i.hams() + "\t" + i.spams() + "\t" + i.uhams() + "\t" + i.uspams());
         }
         File dir_test_file = new File("C:\\Users\\SchronS\\Desktop\\antonis\\enron3(1).tar\\enron3\\enron3\\ham");
         File[] test_files = dir_test_file.listFiles();
         for(File test_file:test_files)
         {
            count_files ++;
            System.out.print("Reading test file " + count_files + "/" + test_files.length);
            result = NaiveBayesClassifier.test(dictionary,test_file);
            if(result.equals("ham"))
            {
               count++; 
            }
         }
         for(File test_file:test_files)
         {
            count_files ++;
            System.out.println("Reading test file " + count_files + "/" + test_files.length);
            result = LogisticRegression.test(dictionary, test_file);
            if(result.equals("ham"))
            {
               count++; 
            }
         }
         System.out.println(count + "/" + test_files.length);
    }
    
    public static List<wordFrequency> learn_ham(List<wordFrequency> dictionary ){
        String new_word = "";
        List<String> unique = new ArrayList();
        int count = 0;
        int k = 0;
        int flag = 0;
        int uflag = 0;
        File dir = new File("./enron1/ham"); 
        File[] indir = dir.listFiles();
        Train.ham_files = indir.length;
        for(File f:indir)
        {
            unique.clear();
            count += 1;
            Train.ham_files = count;
            System.out.println("Reading file " + count + "/" + indir.length);
            try {
                Scanner sc = new Scanner(f);
                while(sc.hasNext())
                { 
                    while(true)
                    {
                        if(sc.hasNext())
                        {
                            flag = 0;
                            new_word = sc.next().replaceAll("[^a-zA-Z]", "");
                            for(String temp:stop_word)
                            {
                                if(new_word.equals(temp) || new_word.length() <= 1)
                                {
                                   flag = 1; 
                                }
                            }
                            if(!new_word.equals("") && flag == 0)
                            {
                              total_ham_words ++;
                              break;
                            }
                        }
                        else
                        {
                            break;
                        }
                    }
                    if(dictionary.isEmpty())
                    {
                        labels.add(1);
                        wordFrequency word = new wordFrequency(new_word,"ham");
                        dictionary.add(word);
                        unique.add(new_word);
                        dictionary.get(0).u_ham();
                    }
                    else
                    {
                        int size = dictionary.size();
                        for(int i = 0 ; i < size; i++)
                        {
                            if(new_word.equalsIgnoreCase(dictionary.get(i).word()))
                            {
                                if(unique.isEmpty())
                                {
                                    unique.add(new_word);
                                    dictionary.get(i).u_ham();
                                }
                                else
                                {
                                    int usize = unique.size();
                                    for(int j = 0; j < usize ; j++)
                                    {
                                        if(new_word.equalsIgnoreCase(unique.get(j)))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            uflag++;
                                            if(uflag == usize)
                                            {
                                               unique.add(new_word);
                                               dictionary.get(i).u_ham();
                                               break;
                                            }
                                        }
                                    }
                                    uflag = 0;
                                }
                                dictionary.get(i).wordDuplicates("ham");
                                break;
                            }
                            else
                            {
                                k++;
                                if(k == size)
                                {    
                                    labels.add(1);
                                    wordFrequency word = new wordFrequency(new_word,"ham");
                                    dictionary.add(word);
                                    unique.add(new_word);
                                    dictionary.get(dictionary.size()-1).u_ham();
                                    break;
                                }
                            }
                        }
                        k = 0;
                    }
                } 
            } catch (FileNotFoundException ex) {
                Logger.getLogger(NaiveBayesClassifier.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        return dictionary;
    }
    
    public static List<wordFrequency> learn_spam(List<wordFrequency> dictionary ){
        String new_word = "";
        int count = 0;
        int k = 0;
        int flag = 0;
        List<String> unique = new ArrayList();
        int uflag = 0;
        File dir = new File("C:\\Users\\SchronS\\Desktop\\antonis\\enron1(1).tar\\enron1\\enron1\\spam");
        File[] indir = dir.listFiles();
        Train.spam_files = indir.length;
        for(File f:indir)
        {
            unique.clear();
            count += 1;
             Train.spam_files = count;
            System.out.println("Reading file " + count + "/" + indir.length);
            try {
                Scanner sc = new Scanner(f);
                while(sc.hasNext())
                { 
                    while(true)
                    {
                        if(sc.hasNext())
                        {
                            flag = 0;
                            new_word = sc.next().replaceAll("[^a-zA-Z]", "");
                            for(String temp:stop_word)
                            {
                                if(new_word.equals(temp) || new_word.length() <= 1)
                                {
                                   flag = 1; 
                                }
                            }
                            if(!new_word.equals("") && flag == 0)
                            {    
                              total_spam_words ++;
                              break;
                            }
                        }
                        else
                        {
                            break;
                        }
                    }
                    if(dictionary.isEmpty())
                    {
                        labels.add(0);
                        wordFrequency word = new wordFrequency(new_word,"spam");
                        dictionary.add(word);
                        unique.add(new_word);
                        dictionary.get(0).u_spam();
                    }
                    else
                    {
                        int size = dictionary.size();
                        for(int i = 0 ; i < size; i++)
                        {
                            if(new_word.equalsIgnoreCase(dictionary.get(i).word()))
                            {
                                if(unique.isEmpty())
                                {
                                    unique.add(new_word);
                                    dictionary.get(i).u_spam();
                                }
                                else
                                {
                                    int usize = unique.size();
                                    for(int j = 0; j < usize ; j++)
                                    {
                                        if(new_word.equalsIgnoreCase(unique.get(j)))
                                        {
                                            break;
                                        }
                                        else
                                        {
                                            uflag++;
                                            if(uflag == usize)
                                            {
                                               unique.add(new_word);
                                               dictionary.get(i).u_spam();
                                               break;
                                            }
                                        }
                                    }
                                    uflag = 0;
                                }
                                dictionary.get(i).wordDuplicates("spam");
                                break;
                            }
                            else
                            {
                                k++;
                                if(k == size)
                                {
                                    labels.add(0);
                                    wordFrequency word = new wordFrequency(new_word,"spam");
                                    dictionary.add(word);
                                    unique.add(new_word);
                                    dictionary.get(dictionary.size()-1).u_spam();
                                    break;
                                }
                            }
                        }
                        k = 0;
                    }
                } 
            } catch (FileNotFoundException ex) {
                Logger.getLogger(NaiveBayesClassifier.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        return dictionary;
    }
    
    public static float[] train_LR(List<wordFrequency> dictionary){
        float[][] features;
        //features = new float[dictionary.size()][2];
        float cost = 0;
        /*for(int i=0;i<dictionary.size();i++)
        {
            features[i][0] = dictionary.get(i).hams();
            features[i][1] = dictionary.get(i).spams();
        }*/
        features = LogisticRegression.normalize(dictionary);
        float[] weights = LogisticRegression.init_weights();
        for(int i=0;i<2000;i++)
        {
            weights = LogisticRegression.SGA(features,labels,weights);
            cost = LogisticRegression.cost(features, labels, weights);
            //System.out.println("cost:" + cost + " weights:" + weights[0] + " " + weights[1]);
        }
        System.out.println("cost:" + cost + " weights:" + weights[0] + " " + weights[1]);
        return weights;
    }
}
