package mod.reborn.server.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

public class ModSerializers {

    public static final DataSerializer<List<String>> STRING_ARRAY = new DataSerializer<List<String>>()
    {
        public void write(PacketBuffer buf, List<String> value)
        {
            buf.writeInt(value == null ? 0 : value.size());
            if(value != null)
                for(String s : value)
                    buf.writeString(s);
        }
        public List<String> read(PacketBuffer buf) throws IOException
        {
            int length = buf.readInt();
            List<String> list = new ArrayList<>();
            for(int i = 0; i < length; i++)
                list.add(buf.readString(32767));
            return list;
        }
        public DataParameter<List<String>> createKey(int id) {return new DataParameter<List<String>>(id, this);}
        public List<String> copyValue(List<String> value) {return value;}
    };
    public static final DataSerializer<Map<UUID, Integer>> UUID_MAP = new DataSerializer<Map<UUID, Integer>>()
    {
        public void write(PacketBuffer buf, Map<UUID, Integer> value)
        {
            buf.writeInt(value.size());
            for(Entry<UUID, Integer> e : value.entrySet())
            {
            	buf.writeUniqueId(e.getKey());
            	buf.writeInt(e.getValue());
            }
        }
        public Map<UUID, Integer> read(PacketBuffer buf) throws IOException
        {
        	Map<UUID, Integer> map = new HashMap<>();
        	int size = buf.readInt();
        	for(int i = 0; i < size; i++)
        	{
        		map.put(buf.readUniqueId(), buf.readInt());
        	}
        	return map;
        }
        public DataParameter<Map<UUID, Integer>> createKey(int id) {return new DataParameter<Map<UUID, Integer>>(id, this);}
        public Map<UUID, Integer> copyValue(Map<UUID, Integer> value) {return value;}
    };

    public static void registerSerializers()
    {
        DataSerializers.registerSerializer(STRING_ARRAY);
        DataSerializers.registerSerializer(UUID_MAP);
    }
}
