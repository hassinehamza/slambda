package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

import org.apache.lucene.search.SearcherManager;
import org.hibernate.stat.QueryStatistics;
import org.infinispan.Cache;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.creson.Factory;
//

import org.infinispan.hotrod.creson.serialization.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.dsl.Query;

import org.infinispan.query.remote.client.MarshallerRegistration;
//import org.infinispan.query.Search;
//import org.infinispan.query.dsl.Query;
//import org.infinispan.query.dsl.QueryFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.Descriptors.FileDescriptor;

import marshellers.RoomMarsheller;

import static org.infinispan.creson.Factory.CRESON_CACHE_NAME;

public class TreasureTest {
    Graph gr;
    Hero superMan;
    Hero batMan;
    Thread b;
    Thread s;

    @BeforeMethod
    public void setUp() throws Exception {
        Factory.get("localhost:11222");
        gr = new Graph(10);
        for (int i = 0; i < gr.getGraphSize(); i++) {
            gr.addUndirectedPath(i, gr.randomRoom(0, 10));
            gr.addUndirectedPath(i, gr.randomRoom(0, 10));
            gr.addUndirectedPath(i, gr.randomRoom(0, 10));
        }

        superMan = new Hero("SuperMan", gr);
        batMan = new Hero("BatMan", gr);
    }

  @Test
  public void work() {
    assert (true);
  }

  @Test
  public void should_rank_heros() throws IOException, DescriptorValidationException {


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



        ConfigurationBuilder builder = new ConfigurationBuilder();
       builder.addServer().host("127.0.0.1").port(11222);


      RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());

    /*this part of marshalling was because an illegalArgumentException
     * " No marshaller registered for org.example.Room  was thrown */

        RemoteCache cache = cacheManager.getCache(CRESON_CACHE_NAME);

//
      System.out.println("creson cache:" + cache.size());
      QueryFactory qf = Search.getQueryFactory(cache);


      /*Query q = qf.create("from org.example.Room o where o.x = 0");
      System.out.println("list" + q.list());*/

      System.out.println("query sent");
  }

}
