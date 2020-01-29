/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naive.bayes.classifier;

/**
 *
 * @author SchronS
 */
public class wordFrequency {
    private String word;
    private int count_spam;
    private int count_ham;
    private int unique_ham;
    private int unique_spam;
    
    wordFrequency(String word,String category){
        this.word = word;
        if(category.equals("spam"))
        {
            this.count_spam = 1;
        }
        else if(category.equals("ham"))
        {
            this.count_ham = 1;
        }
    }
    
    void wordDuplicates(String category){
        if(category.equals("spam"))
        {
            this.count_spam += 1;
        }
        else if(category.equals("ham"))
        {
            this.count_ham += 1;
        }
    }
    
    void u_init_ham(){
        this.unique_ham = 1;
    }
    
    void u_init_spam(){
        this.unique_spam = 1;
    }
    
    void u_ham()
    {
        this.unique_ham++;
    }
    
    void u_spam()
    {
        this.unique_spam++;
    }
    
    String word(){
        return this.word;
    }
    
    int hams(){
        return this.count_ham;
    }
    
    int uhams(){
        return this.unique_ham;
    }
    
    int spams(){
        return this.count_spam;
    }
    
    int uspams(){
        return this.unique_spam;
    }
}
