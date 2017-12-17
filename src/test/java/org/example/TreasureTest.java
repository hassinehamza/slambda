package org.example;

import org.infinispan.creson.Factory;
import org.infinispan.creson.test.Emulation;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class TreasureTest {
  Graph gr;
  Hero superMan;
  Hero batMan;
  Thread b;
  Thread s;

  @BeforeMethod
  public void setUp() throws Exception {
    Factory.get("127.0.0.1:11222");
    gr = new Graph(100);
    for (int i = 0; i < gr.getGraphSize(); i++) {
      gr.addUndirectedPath(i, gr.randomRoom(1, 99));
      gr.addUndirectedPath(i, gr.randomRoom(1, 99));
      gr.addUndirectedPath(i, gr.randomRoom(1, 99));
    }

    superMan = new Hero("SuperMan", gr);
    batMan = new Hero("BatMan", gr);
  }

  @Test
  public void should_rank_heros() {
    s = new Thread(superMan);
    b = new Thread(batMan);
   
    try {
      s.start();
      b.start();
      
      s.join();
      b.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println(superMan.getScore());
    System.out.println(batMan.getScore());
  }

 
}
