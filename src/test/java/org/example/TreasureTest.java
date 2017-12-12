package org.example;

import org.infinispan.creson.test.Emulation;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;


public class TreasureTest extends Emulation {
  Graph gr;
  Hero superMan;
  Hero batMan;
  Thread b;
  Thread s;

  @BeforeMethod
  public void setUp() throws Exception {

    gr = new Graph(1000);
    for (int i = 0; i < gr.getGraphSize(); i++) {
      gr.addUndirectedPath(i, gr.randomRoom(0, 10));
      gr.addUndirectedPath(i, gr.randomRoom(0, 10));
      gr.addUndirectedPath(i, gr.randomRoom(0, 10));
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
      e.printStackTrace();
    }

    System.out.println(superMan.getScore());
    System.out.println(batMan.getScore());
  }

  @Override
  protected int numberOfCaches() {
    return 3;
  }
}
