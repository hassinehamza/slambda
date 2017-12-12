package org.example;

import org.infinispan.creson.Factory;
import org.infinispan.creson.test.Emulation;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

public class TreasureTest {
    Graph gr;
    Hero superMan;
    Hero batMan ; 
    Thread b ; 
    Thread s;
    @BeforeMethod
    public void setUp() throws Exception {
        
        Factory.get("localhost:11222");
        gr = new Graph(1000);
        Random random = new Random(System.currentTimeMillis());
        for(int i = 0 ; i < gr.getGraphSize() ; i++) {
            //if (i>0) gr.addUndirectedPath(i,random.nextInt(i));
            gr.addUndirectedPath(i,gr.randomRoom(0, 10));
            gr.addUndirectedPath(i,gr.randomRoom(0, 10));
            gr.addUndirectedPath(i,gr.randomRoom(0, 10));
            //System.out.println(gr.toString());
        }

        superMan = new Hero("SuperMan", gr);
        batMan = new Hero("BatMan", gr);
    }

    @Test
    public void should_rank_heros() {
        s = new Thread(superMan);
        b = new Thread(batMan);
        s.start();
        b.start();
        try {
          s.join();
          b.join();
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        
        System.out.println(superMan.getScore());
        System.out.println(batMan.getScore());
       // assert superMan.getScore() == gr.nodes.size();
    }

    /*@Override
    protected int numberOfCaches(){
        return 3;
    }*/
}
