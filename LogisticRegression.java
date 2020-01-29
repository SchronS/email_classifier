/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naive.bayes.classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SchronS
 */
public class LogisticRegression {
    
    static float learning_rate = (float) 0.3;
    
    public static String test(List<wordFrequency> dictionary , File test_file){
        float decision_bound = (float) 0.5;
        float prediction = 0;
        int count = 0;
        int y = 0;
        float result = 0;
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
                for(int i = 0 ; i < size ; i++)
                {
                    if(new_word.equalsIgnoreCase(dictionary.get(i).word()))
                    {
                        
                    }
                }
            }   
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NaiveBayesClassifier.class.getName()).log(Level.SEVERE, null, ex);
        }
        prediction = result;  
        System.out.println(prediction);
        if(prediction >= decision_bound)
        {
            System.out.println(" - spam");
            return "spam";
        }
        else
        {
            System.out.println(" - ham");
            return "ham";
        }
    }
    
    public static float[] coefficients(int uhams ,int uspams){
        float[] weight;
        weight = new float[2];
        if(Train.ham_files - uhams != 0)
        {
            weight[0] = (float) (Math.log((float)(Train.spam_files - uspams)/(float)(Train.ham_files - uhams)));
        }
        else
        {
            weight[0] = 0;
        }
        if(uhams != 0)
        {
            weight[1] = (float)(uspams/(float)uhams);
        }
        else
        {
            weight[1] = 0;
        }
        //System.out.println(">>"+weight[0]);
        //System.out.println(">>>"+weight[1]);
        return weight;
    }
    
    public static float logit(int hams , int spams){
        float odds;
        odds = (float) Math.log(hams/(hams+spams)/(spams/(hams+spams)));
        return odds;
    }
    
    public static float hypothesis_func(float z){
        float h = 0;
        z *= -1;
        h = (float)(1/(1+Math.exp((double)z)));
        //System.out.println(">>"+h);
        return h;
    }
    
    public static float inverse_logit(int value ,float[] weights){
        float prob = 0;
        if(value>1)
        {
            value = 1;
        }
        else
        {
            value = 0;
        }
        float odds = weights[0] + weights[1]*value;
        prob = (float) (Math.exp((double)odds)/(1+Math.exp((double)odds)));
        //System.out.println(">>"+prob);
        return prob;
    }
    
    public static float[] init_weights(){
        float[] weights;
        weights = new float[2];
        for(int i = 0; i < weights.length;i++)
        {
            weights[i] = 0;
        }
        return weights;
    }
    
    public static float[][] normalize(List<wordFrequency> dictionary){
        float[][] nvalues;
        int hminval = 9999999;
        int hmaxval = 0;
        int hmean = 0;
        int sminval = 9999999;
        int smaxval = 0;
        int smean = 0;
        int hcount = 0;
        int scount = 0;
        nvalues = new float[dictionary.size()][2];
        for(int i = 0;i<dictionary.size();i++)
        {
            if(dictionary.get(i).hams() > hmaxval)
            {
                hmaxval = dictionary.get(i).hams();
            }
            if(dictionary.get(i).spams() > smaxval)
            {
                smaxval = dictionary.get(i).spams();
            }
            if(dictionary.get(i).hams() < hminval)
            {
                hminval = dictionary.get(i).hams();
            }
            if(dictionary.get(i).spams() < sminval)
            {
                sminval = dictionary.get(i).spams();
            }
            if(dictionary.get(i).hams() != 0)
            {
                hcount++;
            }
            if(dictionary.get(i).spams() != 0)
            {
                scount++;
            }
            hmean += dictionary.get(i).hams();
            smean += dictionary.get(i).spams();
        }
        hmean /= hcount;
        smean /= scount;
        for(int i = 0;i<dictionary.size();i++)
        {
            nvalues[i][0] = (float)(dictionary.get(i).hams() - hmean)/(hmaxval-hminval);
            nvalues[i][1] = (float)(dictionary.get(i).spams() - smean)/(smaxval-sminval);
            //System.out.println(nvalues[i][0] + " " + nvalues[i][1]);
        }
        return nvalues;
    }
    
    public static float[] predict(float[][] features,float[] weights){
        float[] z;
        z = new float[features.length];
        for(int j=0;j < features.length;j++)
        {
           for(int i=0;i < weights.length;i++)
           {
                z[j] += features[j][i]*weights[i];
           }
        }
        for(int i = 0 ;i < z.length;i++)
        {
            //System.out.println(">>" + z[i]);
            z[i] = hypothesis_func(z[i]);
            //System.out.println("->" + z[i]);
        }
        return z;
    }
    
    public static float cost(float[][] features,List<Integer> labels,float[] weights){
        int observations = labels.size();
        float total_cost = 0;
        float[] predictions;
        float[] cost;
        cost = new float[labels.size()];
        float[] hcost;
        hcost = new float[labels.size()];
        float[] scost;
        scost = new float[labels.size()];
        predictions = predict(features,weights);
        for(int i = 0; i < labels.size();i++)
        {
           hcost[i] = (float) (-1*labels.get(i)*Math.log(predictions[i]));
           scost[i] = (float) ((1-labels.get(i))*Math.log(1 - predictions[i]));
           cost[i] =  hcost[i] - scost[i];
        }
        for(int i = 0 ; i < cost.length;i++)
        {
            total_cost += cost[i];
        }
        
        return (float)(total_cost/observations);
    }
    
    public static float[] SGA(float[][] features,List<Integer> labels,float[] weights){
        int count = labels.size();
        float[] predictions;
        float[] gradient;
        gradient = new float[features[0].length];
        predictions = predict(features,weights);
        for(int i = 0; i < features[0].length;i++)
        {
            for(int j = 0; j < features.length ; j++)
            {
                gradient[i] += features[j][i]*(predictions[j] - labels.get(j));
            }
        }
        for(int i = 0;i<gradient.length;i++)
        {
            gradient[i] /= count;
            gradient[i] *= learning_rate;
            weights[i] -= gradient[i];
        }
        return weights;
    }
}
