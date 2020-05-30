package com.illposed.osc.utility;

import com.illposed.osc.AddressSelector;
import com.illposed.osc.OSCBundle;
import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPacket;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class OSCPacketDispatcher {
    private final Map<AddressSelector, OSCListener> selectorToListener = new HashMap();

    public void addListener(AddressSelector addressSelector, OSCListener listener) {
        this.selectorToListener.put(addressSelector, listener);
    }

    public void dispatchPacket(OSCPacket packet) {
        dispatchPacket(packet, null);
    }

    public void dispatchPacket(OSCPacket packet, Date timestamp) {
        if (packet instanceof OSCBundle) {
            dispatchBundle((OSCBundle) packet);
        } else {
            dispatchMessage((OSCMessage) packet, timestamp);
        }
    }

    private void dispatchBundle(OSCBundle bundle) {
        Date timestamp = bundle.getTimestamp();
        for (OSCPacket packet : bundle.getPackets()) {
            dispatchPacket(packet, timestamp);
        }
    }

    private void dispatchMessage(OSCMessage message, Date time) {
        for (Entry<AddressSelector, OSCListener> addrList : this.selectorToListener.entrySet()) {
            if (((AddressSelector) addrList.getKey()).matches(message.getAddress())) {
                ((OSCListener) addrList.getValue()).acceptMessage(time, message);
            }
        }
    }
}
