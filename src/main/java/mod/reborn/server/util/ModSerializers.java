package mod.reborn.server.util;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.network.datasync.DataSerializers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    public static void registerSerializers()
    {
        DataSerializers.registerSerializer(STRING_ARRAY);
    }
}
