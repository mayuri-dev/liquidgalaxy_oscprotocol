package com.illposed.osc;

import java.util.Date;

public interface OSCListener {
    void acceptMessage(Date date, OSCMessage oSCMessage);
}
