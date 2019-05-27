package com.laughitover.kubku.commonUtils.security;

import com.fasterxml.uuid.EthernetAddress;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import org.apache.commons.lang3.StringUtils;

public class UUIDUtils {
    static private EthernetAddress nic = EthernetAddress.fromInterface();
    static private TimeBasedGenerator generator = Generators.timeBasedGenerator(nic);

    static public String generateTimeBased() {
        return StringUtils.join(StringUtils.split(generator.generate().toString(), '-'));
    }
}
