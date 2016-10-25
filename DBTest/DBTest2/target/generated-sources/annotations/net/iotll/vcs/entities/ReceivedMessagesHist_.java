package net.iotll.vcs.entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2016-10-25T13:01:44")
@StaticMetamodel(ReceivedMessagesHist.class)
public class ReceivedMessagesHist_ { 

    public static volatile SingularAttribute<ReceivedMessagesHist, Date> dateTime;
    public static volatile SingularAttribute<ReceivedMessagesHist, Integer> idMessage;
    public static volatile SingularAttribute<ReceivedMessagesHist, String> message;

}