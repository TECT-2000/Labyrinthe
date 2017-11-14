
package labyrinthe;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Mike Tsebo
 */
public class Labyrinthe {
    
    static ArrayList posSorties=new ArrayList();
    static ArrayList posEntree=new ArrayList() ;
    static char [][] matriceElts;
    static ArrayList<EtatCourant> open=new ArrayList();
    static ArrayList<EtatCourant> closed=new ArrayList();  
    static ArrayList<EtatCourant> chainage=new ArrayList();
    
    public static void main(String[] args) throws IOException {
        
        //on initialise la matrice
        
        matriceElts=initialiseMatrice();
        
        //on vérifie si le labyrinthe est bien formé
        //les elts de chemin ne doivent pas se trouver aux extrémités rien que l'entrée, les sorties et le mur;
        //les murs peuvent etre partout 
        //l'entrée au début 
        if(posSorties.isEmpty()){
            System.out.println("pas de sorties");  
            System.out.println("Entrer des * pour marquer les sorties dans le labyrinthe"); 
            System.exit(0);
        }
        
        if(posEntree.isEmpty()){
            System.out.println("pas d'entrée");
            System.out.println("Entrer + pour marquer l'unique entrée du le labyrinthe");
            System.exit(0);}
        else{
            //posEntree ={0,0};
           
        EtatCourant entree=new EtatCourant((int)posEntree.get(0),(int) posEntree.get(1),posSorties,posEntree);
        entree.succession((int)posEntree.get(0),(int) posEntree.get(1), posSorties, posEntree,"rien");
        entree.ancetre=entree;
        open.add(entree);
       
        System.out.println(RechercheAEtoile());
                    }
        }
      
    /**initialisation de la matrice
        * # mur
        * * sortie
        * - elt de chemin 
        * + point de départ
         */
    
    public static char[] [] initialiseMatrice() throws IOException{ //retourne la matrice;
        
         //on compte le nombre de lignes et de colonnes du fichier 
         //pour déterminer la taille de la matrice   
        BufferedReader out=new BufferedReader(new FileReader("map.txt"));
        String line=" ";
         int nligne=0;
         int ncol=0;
        while((line=out.readLine()) !=null){
            nligne++;
            ncol=line.length();
        }
        out.close();
        
        /**on lit le fichier
         * et on initialise la matrice :
         * * # mur
         * * sortie
         * - elt de chemin 
         * + point de départ
         */
        BufferedReader in=new BufferedReader(new FileReader("map.txt"));
        String ligne=" ";
        
        int numéroLigne=0; //le numéro d'une ligne du fichier
        char [][] matriceElts=new char[nligne][ncol];
        
        //initialise de la matrice 
        while((ligne=in.readLine()) !=null){
            for(int j=0;j<ligne.length();j++){
                    
                    matriceElts[numéroLigne][j]=ligne.charAt(j);
                    
                    if(ligne.charAt(j)=='+'){
                        //on a repéré le point d'entrée du labyrinthe
                        posEntree.add(numéroLigne);
                        posEntree.add(j);
                    }
                    if(ligne.charAt(j)=='*'){
                        //on a repéré une sortie du labyrinthe
                        posSorties.add(numéroLigne);
                        posSorties.add(j);
                    }
                    
        }
            numéroLigne++;
            }
        
        in.close();
        
        //on affiche la matrice contenant le labyrinthe
        for(int i=0;i<nligne;i++){
            for(int j=0;j<ncol;j++){
                System.out.print(matriceElts[i][j]);
            }
            System.out.println(" ");
        }
            return matriceElts;
            }
    
    public static String RechercheAEtoile(){
        
       while(!open.isEmpty()){
           
            int f=open.get(0).getF();//valeur de min de f
            int pos=0; //position dans open du point qui a la plus petite valeur de f
            for(int i=1;i<open.size();i++){
                if(open.get(i).getF()<f)
                     pos=i;
              }
            EtatCourant pris=open.get(pos);
            String action="";
            //action permettant de partir de l'ancetre au noeud courant
            //ceci permet d'enlever l'ancetre parmi les successeurs du noeud
            if(pris.getAbscisse()-pris.ancetre.getAbscisse()==1)
                 action="bas";
            if(pris.getAbscisse()-pris.ancetre.getAbscisse()==-1)
                action="haut";
            if(pris.getOrdonnee()-pris.ancetre.getOrdonnee()==1)
                 action="droite";
            if(pris.getOrdonnee()-pris.ancetre.getOrdonnee()==-1)
                action="gauche";
            if(pris.getOrdonnee()-pris.ancetre.getOrdonnee()==0 && pris.getAbscisse()-pris.ancetre.getAbscisse()==0)
                action="rien";
            System.out.println(action);
            
            
            if(matriceElts[pris.getAbscisse()][pris.getOrdonnee()]=='*'){
                //on a trouvé la sortie la plus proche
                String s=" ";
                s+=pris.toString();
                chainage.add(pris);      //on l'ajoute au chainage
                int i=chainage.size()-1; //position du point de sortie dans chainage
                
                 //on doit afficher le chemin
                 //pour cela on sélectionne dans le chainage les ancetres du point de sortie 
                while(i!=0){
                  for(int j=i;j>0;j--){
                      if(chainage.get(j-1)==chainage.get(i).ancetre){
                          i=j-1;
                          s=chainage.get(j-1)+"->"+s;
                      }
                          }
                  if(i==chainage.size()-1)
                      i=0;
                }
                
                 return "succès chemin :"+s;
            }
            pris.succession(open.get(pos).getAbscisse(), open.get(pos).getOrdonnee(), posSorties, posEntree,action);
            open.remove(pos);
            closed.add(pris);
            
            if(pris.successeurs.isEmpty()){
                //s'il le noeud n'a pas de successeurs on revient au while
                continue;
            }
            
            System.out.println("taille des successeurs de "+pris.toString() +" : "+pris.successeurs.size());
            System.out.println("successeurs de "+pris.toString());
            
            //affiche les successeurs du noeud sélectionné dans open
            for(int i=0;i<pris.successeurs.size();i+=1){
                    pris.successeurs.get(i).ancetre=pris;  //à chaque successeur, on met le noeud sélectionné comme ancetre direct
                    System.out.println(pris.successeurs.get(i).toString());
                    if(!contient(open,pris.successeurs.get(i)) && !contient(closed,pris.successeurs.get(i))){
                        open.add(pris.successeurs.get(i));
                        System.out.println(open);
                        
                    }
                    
                 }
             chainage.add(pris);  //on ajoute le noeud sélectionné dans le chainage
        }
       return "impossible"; //open est vide
    }
   
   static public boolean contient(ArrayList<EtatCourant>list,EtatCourant elt){
       
  //cette nméthode est en quelque sorte une rédéfinition de la methode contains d'une arraylist
     boolean rep=false;
     for(int i=0;i<list.size();i++){
         if(list.get(i).getAbscisse()==elt.getAbscisse() && list.get(i).getOrdonnee()==elt.getOrdonnee())
             rep=true;
     }
             return rep;
   }
   
    /*static public boolean bienForme(){
       boolean rep=true;
        //on vérifie si le labyrinthe est bien formé
        //les elts de chemin ne doivent pas se trouver aux extrémités seuls l'entrée, les sorties et le mur le peuvent;
        
        //on verifie si les elts de chemin sont aux extrémités
        //si c'est le cas la fonction renvoie un message d'erreur et le programme s'arrête
        for(int i=0;i<matriceElts[0].length;i++){
            if(matriceElts[0][i]=='-'){
                System.out.println("labyrinthe mal formé");
                System.out.println("les elts de chemin ne doivent pas être aux extrémités");
                rep=false;
                System.exit(0);
              }
         }
        for(int i=0;i<matriceElts[matriceElts.length-1].length;i++){
            if(matriceElts[matriceElts.length-1][i]=='-'){
                System.out.println("labyrinthe mal formé");
                System.out.println("les elts de chemin ne doivent pas être aux extrémités");
                rep=false;
                System.exit(0);
              }
         }
        //on verifie sur la première colonne
        for(int i=0;i<matriceElts.length;i++){
            if(matriceElts[i][0]=='-'){
                System.out.println("labyrinthe mal formé");
                System.out.println("les elts de chemin ne doivent pas être aux extrémités");
                rep=false;
                System.exit(0);
              }
            
         }
        //on vérifie sur la dernière colonne
        for(int i=0;i<matriceElts.length;i++){
            if(matriceElts[i][matriceElts[0].length-1]=='-'){
                System.out.println("labyrinthe mal formé");
                System.out.println("les elts de chemin ne doivent pas être aux extrémités");
                rep=false;
                System.exit(0);
              }
         }
        
        //on vérifie s'il y a une sortie dans le labyrinthe
        //s'il n'y a pas la fonction retourne un message d'erreur et le programme s'arrete
        if(posSorties.isEmpty()){
            rep=false;
            System.out.println("Impossible à résoudre : pas de sortie ");
        }
        
        if((posEntree[0]!=0 || posEntree[0]!=matriceElts.length-1) && !(posEntree[1]==0 || posEntree[1]==matriceElts[0].length-1)){
            rep=false;
            System.out.println("l'entrée doit etre aux extrémités");
        }
        return rep;
    }*/
}
