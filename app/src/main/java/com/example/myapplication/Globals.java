package com.example.myapplication;

public class Globals {

    public static String hostHealthCare = "schmiederm.selfhost.eu";
    public static String portHealthCare = "5000";



    public static String qSaveContactPerson = "Speichern\n\n" //
            + "Wollen Sie die Daten Ihrer Kontaktperson aktualisieren?\n"    //
            + "Drücken Sie auf Ja, wenn Sie diese aktualisieren möchten.\n"    //
            + "Drücken Sie auf Nein, um Ihre Eingaben nochmals einzusehen.";

    public static String qCancelContactPerson = "Abbrechen\n\n" //
            + "Wollen Sie Ihre alten Daten beibehalten?\n"    //
            + "Drücken Sie auf Ja, um diese zu behalten.\n"    //
            + "Drücken Sie auf Nein, um Ihre Eingaben nochmals einzusehen.";

    public static String qSendVitalParam = "" //
            + "Senden\n\n"   //
            + "Wollen Sie Ihre Gesundheitsdaten übermitteln?\n" //
            + "Drücken Sie Ja um Ihre Daten zu übermitteln.\n"  //
            + "Drücken Sie Nein um Ihre Daten nochmals zu überprüfen.";

    public static String qSavePatientData = "Speichern\n\n" //
            + "Wollen Sie Ihre Daten aktualisieren?\n"    //
            + "Drücken Sie auf Ja, wenn Sie diese aktualisieren möchten.\n"    //
            + "Drücken Sie auf Nein, um Ihre Eingaben nochmals einzusehen.";

    public static String qCancelPatientData = "Abbrechen\n\n" //
            + "Wollen Sie Ihre alten Daten beibehalten?\n"    //
            + "Drücken Sie auf Ja, um diese zu behalten.\n"    //
            + "Drücken Sie auf Nein, um Ihre Eingaben nochmals einzusehen.";

    

    public static String USE_UID_AS_USER = "TTW8PLIAjWZUrl92ap5DHyv3gx42"; //FIXME make null
    public static boolean filterTrendsPerDay = true; //FIXME make true
    public static boolean useDummyVitalValues = false; //FIXME make false



    static {
        if ( useDummyVitalValues ) {
            hostHealthCare = "10.0.2.2";
            portHealthCare = "65500";
        }
    }


}
