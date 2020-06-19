package com.example.myapplication;

public class Globals {

    public static String hostHealthCare = "schmiederm.selfhost.eu";
    public static String portHealthCare = "5000";
    public static String port2HealthCare = "3000";


    public final static String qSaveContactPerson = "Speichern\n\n" //
            + "Wollen Sie die Daten Ihrer Kontaktperson aktualisieren?\n"    //
            + "Drücken Sie auf Ja, wenn Sie diese aktualisieren möchten.\n"    //
            + "Drücken Sie auf Nein, um Ihre Eingaben nochmals einzusehen.";

    public final static String qCancelContactPerson = "Abbrechen\n\n" //
            + "Wollen Sie Ihre alten Daten beibehalten?\n"    //
            + "Drücken Sie auf Ja, um diese zu behalten.\n"    //
            + "Drücken Sie auf Nein, um Ihre Eingaben nochmals einzusehen.";

    public final static String qSendVitalParam = "" //
            + "Senden\n\n"   //
            + "Wollen Sie Ihre Gesundheitsdaten übermitteln?\n" //
            + "Drücken Sie Ja um Ihre Daten zu übermitteln.\n"  //
            + "Drücken Sie Nein um Ihre Daten nochmals zu überprüfen.";

    public final static String qClearVitalParam = "" //
            + "Leeren\n\n"   //
            + "Wollen Sie Ihre Eingaben leeren?\n" //
            + "Drücken Sie Ja um Ihre Eingaben zu leeren.\n"  //
            + "Drücken Sie Nein um Ihre Eingaben nochmals zu überprüfen.";

    public final static String qSavePatientData = "Speichern\n\n" //
            + "Wollen Sie Ihre Daten aktualisieren?\n"    //
            + "Drücken Sie auf Ja, wenn Sie diese aktualisieren möchten.\n"    //
            + "Drücken Sie auf Nein, um Ihre Eingaben nochmals einzusehen.";

    public final static String qCancelPatientData = "Abbrechen\n\n" //
            + "Wollen Sie Ihre alten Daten beibehalten?\n"    //
            + "Drücken Sie auf Ja, um diese zu behalten.\n"    //
            + "Drücken Sie auf Nein, um Ihre Eingaben nochmals einzusehen.";

    

    public final static String USE_UID_AS_USER = null; //"TTW8PLIAjWZUrl92ap5DHyv3gx42"; //FIXME make null
    public final static boolean filterTrendsPerDay = true; //FIXME make true
    public final static boolean useDummyVitalValues = false; //FIXME make false



    static {
        if ( useDummyVitalValues ) {
            hostHealthCare = "10.0.2.2";
            portHealthCare = "65500";
            port2HealthCare = "65500";
        }
    }


}
