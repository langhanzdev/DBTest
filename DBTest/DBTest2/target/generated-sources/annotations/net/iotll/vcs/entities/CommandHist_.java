package net.iotll.vcs.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import net.iotll.vcs.entities.Lpcs;
import net.iotll.vcs.entities.Scs;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-21T16:59:13")
@StaticMetamodel(CommandHist.class)
public class CommandHist_ { 

    public static volatile SingularAttribute<CommandHist, Date> dateTime;
    public static volatile SingularAttribute<CommandHist, Scs> sc;
    public static volatile SingularAttribute<CommandHist, String> category;
    public static volatile SingularAttribute<CommandHist, String> type;
    public static volatile SingularAttribute<CommandHist, String> message;
    public static volatile SingularAttribute<CommandHist, String> value;
    public static volatile SingularAttribute<CommandHist, Lpcs> lpc;
    public static volatile SingularAttribute<CommandHist, Integer> idCommandHist;
    public static volatile SingularAttribute<CommandHist, String> status;

}