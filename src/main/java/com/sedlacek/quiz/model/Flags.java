package com.sedlacek.quiz.model;

import java.util.HashMap;
import java.util.Map;

public class Flags {

    private Flags() {
    }

    public static final Map<String, String> Europe = Map.ofEntries(
            Map.entry("AL", "Albánie"),
            Map.entry("AD", "Andorra"),
            Map.entry("AM", "Arménie"),
            Map.entry("AZ", "Ázerbájdžán"),
            Map.entry("BE", "Belgie"),
            Map.entry("BY", "Bělorusko"),
            Map.entry("BA", "Bosna a Hercegovina"),
            Map.entry("BG", "Bulharsko"),
            Map.entry("ME", "Černá Hora"),
            Map.entry("CZ", "Česká republika"),
            Map.entry("DK", "Dánsko"),
            Map.entry("EE", "Estonsko"),
            Map.entry("FI", "Finsko"),
            Map.entry("FR", "Francie"),
            Map.entry("GE", "Gruzie"),
            Map.entry("HR", "Chorvatsko"),
            Map.entry("IE", "Irsko"),
            Map.entry("IS", "Island"),
            Map.entry("IT", "Itálie"),
            Map.entry("KZ", "Kazachstán"),
            Map.entry("CY", "Kypr"),
            Map.entry("LI", "Lichtenštejnsko"),
            Map.entry("LT", "Litva"),
            Map.entry("LV", "Lotyšsko"),
            Map.entry("LU", "Lucembursko"),
            Map.entry("HU", "Maďarsko"),
            Map.entry("MT", "Malta"),
            Map.entry("MD", "Moldavsko"),
            Map.entry("MC", "Monako"),
            Map.entry("DE", "Německo"),
            Map.entry("NL", "Nizozemsko"),
            Map.entry("NO", "Norsko"),
            Map.entry("PL", "Polsko"),
            Map.entry("PT", "Portugalsko"),
            Map.entry("AT", "Rakousko"),
            Map.entry("RO", "Rumunsko"),
            Map.entry("RU", "Rusko"),
            Map.entry("GR", "Řecko"),
            Map.entry("SM", "San Marino"),
            Map.entry("MK", "Severní Makedonie"),
            Map.entry("SK", "Slovensko"),
            Map.entry("SI", "Slovinsko"),
            Map.entry("GB", "Spojené království"),
            Map.entry("RS", "Srbsko"),
            Map.entry("ES", "Španělsko"),
            Map.entry("SE", "Švédsko"),
            Map.entry("CH", "Švýcarsko"),
            Map.entry("TR", "Turecko"),
            Map.entry("UA", "Ukrajina"),
            Map.entry("VA", "Vatikán")
    );

    public static final Map<String, String> AsiaAndOceania = Map.<String, String>ofEntries(
            Map.entry("AF", "Afghánistán"),
            Map.entry("AM", "Arménie"),
            Map.entry("AU", "Austrálie"),
            Map.entry("AZ", "Ázerbájdžán"),
            Map.entry("BH", "Bahrajn"),
            Map.entry("BD", "Bangladéš"),
            Map.entry("BT", "Bhútán"),
            Map.entry("BN", "Brunej"),
            Map.entry("CN", "Čína"),
            Map.entry("EG", "Egypt"),
            Map.entry("PH", "Filipíny"),
            Map.entry("GE", "Gruzie"),
            Map.entry("IN", "Indie"),
            Map.entry("ID", "Indonésie"),
            Map.entry("IQ", "Irák"),
            Map.entry("IR", "Írán"),
            Map.entry("IL", "Izrael"),
            Map.entry("JP", "Japonsko"),
            Map.entry("YE", "Jemen"),
            Map.entry("KR", "Jižní Korea"),
            Map.entry("JO", "Jordánsko"),
            Map.entry("KH", "Kambodža"),
            Map.entry("QA", "Katar"),
            Map.entry("KZ", "Kazachstán"),
            Map.entry("KW", "Kuvajt"),
            Map.entry("CY", "Kypr"),
            Map.entry("KG", "Kyrgyzstán"),
            Map.entry("LA", "Laos"),
            Map.entry("LB", "Libanon"),
            Map.entry("MY", "Malajsie"),
            Map.entry("MV", "Maledivy"),
            Map.entry("MN", "Mongolsko"),
            Map.entry("MM", "Myanmar"),
            Map.entry("OM", "Omán"),
            Map.entry("NP", "Nepál"),
            Map.entry("NZ", "Nový Zéland"),
            Map.entry("PK", "Pákistán"),
            Map.entry("RU", "Rusko"),
            Map.entry("SA", "Saúdská Arábie"),
            Map.entry("KP", "Severní Korea"),
            Map.entry("SG", "Singapur"),
            Map.entry("AE", "Spojené arabské emiráty"),
            Map.entry("LK", "Srí Lanka"),
            Map.entry("SY", "Sýrie"),
            Map.entry("TJ", "Tádžikistán"),
            Map.entry("TH", "Thajsko"),
            Map.entry("TW", "Tchaj-wan"),
            Map.entry("TR", "Turecko"),
            Map.entry("TM", "Turkmenistán"),
            Map.entry("UZ", "Uzbekistán"),
            Map.entry("VN", "Vietnam"),
            Map.entry("TL", "Východní Timor")
    );

    public static final Map<String, String> NorthAndSouthAmerica = Map.ofEntries(
            Map.entry("CA", "Kanada"),
            Map.entry("US", "Spojené státy americké"),
            Map.entry("MX", "Mexiko"),
            Map.entry("BZ", "Belize"),
            Map.entry("GT", "Guatemala"),
            Map.entry("HN", "Honduras"),
            Map.entry("CR", "Kostarika"),
            Map.entry("NI", "Nikaragua"),
            Map.entry("PA", "Panama"),
            Map.entry("SV", "Salvador"),
            Map.entry("DO", "Dominikánská republika"),
            Map.entry("JM", "Jamajka"),
            Map.entry("CU", "Kuba"),
            Map.entry("AR", "Argentina"),
            Map.entry("BO", "Bolívie"),
            Map.entry("BR", "Brazílie"),
            Map.entry("EC", "Ekvádor"),
            Map.entry("GY", "Guyana"),
            Map.entry("CL", "Chile"),
            Map.entry("CO", "Kolumbie"),
            Map.entry("PY", "Paraguay"),
            Map.entry("PE", "Peru"),
            Map.entry("SR", "Surinam"),
            Map.entry("VE", "Venezuela")
    );

    public static final Map<String, String> Africa = Map.<String, String>ofEntries(
            Map.entry("DZ", "Alžírsko"),
            Map.entry("AO", "Angola"),
            Map.entry("BJ", "Benin"),
            Map.entry("BW", "Botswana"),
            Map.entry("BI", "Burundi"),
            Map.entry("TD", "Čad"),
            Map.entry("DJ", "Džibutsko"),
            Map.entry("EG", "Egypt"),
            Map.entry("ER", "Eritrea"),
            Map.entry("ET", "Etiopie"),
            Map.entry("GA", "Gabon"),
            Map.entry("GM", "Gambie"),
            Map.entry("GH", "Ghana"),
            Map.entry("GN", "Guinea"),
            Map.entry("GW", "Guinea-Bissau"),
            Map.entry("ZA", "Jihoafrická republika"),
            Map.entry("SS", "Jižní Súdán"),
            Map.entry("CM", "Kamerun"),
            Map.entry("CV", "Kapverdy"),
            Map.entry("KE", "Keňa"),
            Map.entry("KM", "Komory"),
            Map.entry("CG", "Konžská republika"),
            Map.entry("CD", "Konžská demokratická republika"),
            Map.entry("LS", "Lesotho"),
            Map.entry("LR", "Libérie"),
            Map.entry("LY", "Libye"),
            Map.entry("MG", "Madagaskar"),
            Map.entry("MW", "Malawi"),
            Map.entry("ML", "Mali"),
            Map.entry("MA", "Maroko"),
            Map.entry("MU", "Mauricius"),
            Map.entry("MR", "Mauritánie"),
            Map.entry("MZ", "Mosambik"),
            Map.entry("NA", "Namibie"),
            Map.entry("NE", "Niger"),
            Map.entry("NG", "Nigérie"),
            Map.entry("CI", "Pobřeží Slonoviny"),
            Map.entry("GQ", "Rovníková Guinea"),
            Map.entry("RW", "Rwanda"),
            Map.entry("SN", "Senegal"),
            Map.entry("SC", "Seychely"),
            Map.entry("SL", "Sierra Leone"),
            Map.entry("SO", "Somálsko"),
            Map.entry("CF", "Středoafrická republika"),
            Map.entry("SD", "Súdán"),
            Map.entry("ST", "Svatý Tomáš a Princův ostrov"),
            Map.entry("SZ", "Svazijsko"),
            Map.entry("TZ", "Tanzanie"),
            Map.entry("TG", "Togo"),
            Map.entry("TN", "Tunisko"),
            Map.entry("UG", "Uganda"),
            Map.entry("ZM", "Zambie"),
            Map.entry("ZW", "Zimbabwe")
    );

    public static Map<String, String> getAllFlagsAndStates() {
        Map<String, String> allFlagsAndStates = new HashMap<>();

        allFlagsAndStates.putAll(Europe);
        allFlagsAndStates.putAll(AsiaAndOceania);
        allFlagsAndStates.putAll(NorthAndSouthAmerica);
        allFlagsAndStates.putAll(Africa);

        return allFlagsAndStates;
    }
}
