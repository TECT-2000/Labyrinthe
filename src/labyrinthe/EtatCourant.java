
package labyrinthe;

import static java.lang.Math.abs;
import java.util.ArrayList;

/**
 *
 * @author Mike Tsebo
 */
public class EtatCourant  {
    
        private int abscisse;
        private int ordonnee;
        private int h; //valeur de la fonction heuristique
        private int g;
        private int f;
        EtatCourant ancetre;
        ArrayList<EtatCourant> successeurs=new ArrayList();
        
    public int getOrdonnee() {
        return ordonnee;
    }

    public void setOrdonnee(int ordonnee) {
        this.ordonnee = ordonnee;
    }

    public int getAbscisse() {
        return abscisse;
    }

    public void setAbscisse(int abscisse) {
        this.abscisse = abscisse;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getF() {
        return f;
    }

    public void setF(int f) {
        this.f = f;
    }
     
    public int fonctionG(ArrayList posEntree){
    
     return abs((int)posEntree.get(0)- this.abscisse)+ abs((int)posEntree.get(1)-this.ordonnee);
    }
    
    public int heuristique(ArrayList list){
        
        int he=abs(this.abscisse- (int) list.get(0))+ abs(this.ordonnee-(int) list.get(1));
        for(int i=0;i<=list.size()/2;i+=2){
            if(he>abs(this.abscisse- (int) list.get(i))+ abs(this.ordonnee- (int) list.get(i+1)))
                he=abs(this.abscisse- (int) list.get(i))+ abs(this.ordonnee- (int) list.get(i+1));
        }
        return he;
    } 
    
    public void succession(int x, int y,ArrayList list,ArrayList posEntree, String action){ //calcule les successeur et ne s'applique qu'à l'entrée
       
        //successeur haut
      if(action!=Actions.bas.toString()){  
            if(x!=0 && Labyrinthe.matriceElts[x-1][y]!='#'){
             successeurs.add(new EtatCourant(x-1, y, list, posEntree));
            }
      }
       //successeur bas
       if(action!=Actions.haut.toString()  ){ 
            if(x!=Labyrinthe.matriceElts.length-1  && Labyrinthe.matriceElts[x+1][y]!='#'){
                  successeurs.add(new EtatCourant(x+1, y, list, posEntree));}
            }
       
       //successeur gauche
       if(action!=Actions.droite.toString() ){ 
            if(y!=0 && Labyrinthe.matriceElts[x][y-1]!='#'){
             successeurs.add(new EtatCourant(x, y-1, list, posEntree));
            }
       }
       //successeur droite
       if(action!=Actions.gauche.toString()  ){ 
            if( y!=Labyrinthe.matriceElts[0].length-1 && Labyrinthe.matriceElts[x][y+1]!='#'){
                successeurs.add(new EtatCourant(x, y+1, list, posEntree));
            }
       }
    }
    
    @Override
   public String toString(){
        return "("+this.abscisse+","+this.ordonnee+")";
    }
   
   public EtatCourant(int abscisse, int ordonnee,ArrayList list,ArrayList  posEntree) {
        this.abscisse = abscisse;
        this.ordonnee = ordonnee;
        this.g = fonctionG(posEntree);
        this.h=heuristique(list);
        this.f=this.g+this.h;
    }
}
