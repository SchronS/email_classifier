package naive.bayes.classifier;

import java.io.File;
import java.io.FileNotFoundException;
import static java.lang.Math.log;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NaiveBayesClassifier{
    
    public static String test(List<wordFrequency> dictionary , File test_file){
        float prior_ham = (float)Train.total_ham_words/(Train.total_spam_words+Train.total_ham_words);
        float prior_spam = (float)Train.total_spam_words/(Train.total_spam_words+Train.total_ham_words);
        float likelihood_ham = (float) log(prior_ham);
        float likelihood_spam = (float) log(prior_spam);
        String new_word = "";
        try {
            Scanner sc = new Scanner(test_file);
            while(sc.hasNext())
            {
                while(true)
                {
                    if(sc.hasNext())
                    {
                        new_word = sc.next().replaceAll("[^a-zA-Z]", "");
                        if(!new_word.equals(""))
                        {    
                            break;
                        }
                    }
                    else
                    {
                        break;
                    }
                }
                int size = dictionary.size();
                for(int i = 0 ; i < size; i++)
                {
                    if(new_word.equalsIgnoreCase(dictionary.get(i).word()))
                    {
                        likelihood_ham += log((float)(dictionary.get(i).hams()+1)/(Train.total_ham_words + dictionary.size()));
                        likelihood_spam += log((float)(dictionary.get(i).spams()+1)/(Train.total_spam_words + dictionary.size()));
                        break;
                    }
                }
            }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NaiveBayesClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(likelihood_ham*prior_ham/nc);
        //System.out.println(likelihood_spam*prior_spam/nc);
        if(likelihood_ham > likelihood_spam)
        {
            System.out.println(" - ham");
            return "ham";
        }
        else
        {
            System.out.println(" - spam");
            return "spam";
        }
    }
}
