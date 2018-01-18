package org.example;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.hibernate.stat.QueryStatistics;
import org.infinispan.Cache;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.Search;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.client.hotrod.impl.RemoteCacheImpl;
import org.infinispan.client.hotrod.marshall.ProtoStreamMarshaller;
import org.infinispan.creson.Factory;
import org.infinispan.creson.test.Emulation;
import org.infinispan.protostream.FileDescriptorSource;
import org.infinispan.protostream.SerializationContext;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;
import org.infinispan.query.remote.client.MarshallerRegistration;
//import org.infinispan.query.Search;
//import org.infinispan.query.dsl.Query;
//import org.infinispan.query.dsl.QueryFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.Descriptors.FileDescriptor;

import marshellers.RoomMarsheller;

public class TreasureTest {
  Graph gr;
  Hero superMan;
  Hero batMan;
  Thread b;
  Thread s;

  @BeforeMethod
  public void setUp() throws Exception {
    Factory.get("127.0.0.1:11222");
    gr = new Graph(10);
    for (int i = 0; i < gr.getGraphSize(); i++) {
      gr.addUndirectedPath(i, gr.randomRoom(1, 9));
      gr.addUndirectedPath(i, gr.randomRoom(1, 9));
      /* gr.addUndirectedPath(i, gr.randomRoom(1, 9)); */
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
    ConfigurationBuilder builder = new ConfigurationBuilder();
    builder.addServer().host("127.0.0.1").port(11222).marshaller(new ProtoStreamMarshaller());
    RemoteCacheManager cacheManager = new RemoteCacheManager(builder.build());
    
    /*this part of marshalling was because an illegalArgumentException 
     * " No marshaller registered for org.example.Room  was thrown*/
    SerializationContext ctx = ProtoStreamMarshaller.getSerializationContext(cacheManager);
    ctx.registerProtoFiles(FileDescriptorSource.fromResources("library.proto"));
    ctx.registerMarshaller(new RoomMarsheller());

    RemoteCache<?, ?> cache = cacheManager.getCache();

    System.out.println("creson cache:" + cache);
    QueryFactory queryFactory = Search.getQueryFactory(cache);

    Query q = queryFactory.from(Room.class)
        .having("treasure").eq(1)
        .build();

    System.out.println("query : " + q);
    List<Room> list = q.list();
    System.out.println("not taken " + list.size());

    try {
      s.start();
      b.start();

      s.join();
      b.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("the first Hero got " + superMan.getScore() + " Treasures");
    System.out.println("the first Hero got " + batMan.getScore() + " Treasures");
  }

}
