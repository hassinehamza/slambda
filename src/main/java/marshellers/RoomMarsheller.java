package marshellers;

import java.io.IOException;
import java.util.LinkedList;

import org.example.Room;
import org.infinispan.protostream.MessageMarshaller;

public class RoomMarsheller implements MessageMarshaller<Room> {

  @Override
  public Class<? extends Room> getJavaClass() {
    return Room.class;
  }

  @Override
  public String getTypeName() {
    return "Room";
  }

  @Override
  public Room readFrom(org.infinispan.protostream.MessageMarshaller.ProtoStreamReader reader)
      throws IOException {
      System.out.println("hello reader");
      int id = reader.readInt("id");
      int treasure = reader.readInt("treasure");
     LinkedList<Room> adjList = reader.readCollection("adjList", new LinkedList<>(), Room.class);
     return new Room(id);
  }

  @Override
  public void writeTo(org.infinispan.protostream.MessageMarshaller.ProtoStreamWriter writer,
      Room room) throws IOException {
      System.out.println("hellooo writer");
      writer.writeInt("id", room.getId() );
      writer.writeInt("treasure", room.getTreasure());
      writer.writeCollection("adjList", room.getAdjList(), Room.class);
  }

}
