package Parse;

import org.objectweb.asm.Opcodes;

/**
 * Created by budocf on 12/17/2015.
 */
public abstract class IData {
    protected String name = null;
    protected String accessibility = null;

    public static String GetAccess(int access) {
        if ((access & Opcodes.ACC_PUBLIC) != 0) {
            return "public";
        } else if ((access & Opcodes.ACC_PROTECTED) != 0) {
            return "protected";
        } else if ((access & Opcodes.ACC_PRIVATE) != 0) {
            return "private";
        } else {
            return "default";
        }
    }

    public void setAccess(String access) {
        this.accessibility = access;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
