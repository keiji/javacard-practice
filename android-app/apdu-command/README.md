APDU Command helper for Java
========
Helper classes for building APDU command.

Download
--------

```groovy
repositories {
    maven { url "https://s01.oss.sonatype.org/content/repositories/devkeijiapdu-1011" }
}

dependencies {
    implementation('dev.keiji.apdu:apdu-command:0.9.0')
}
```

## References
 * ISO 7816-4, Section 5 - Basic Organizations
   * https://cardwerk.com/smart-card-standard-iso7816-4-section-6-basic-interindustry-commands/
 * Functional Specification of the OpenPGP application on ISO Smart Card Operating Systems - v3.4.1
   * https://gnupg.org/ftp/specs/OpenPGP-smart-card-application-3.4.pdf
 * 在留カード等仕様書（一般公開用） Ver 1.3 - 法務省入国管理局出入国管理情報官
   * https://www.moj.go.jp/isa/content/930001497.pdf
 * 運転免許証及び運転免許証作成システム等仕様書（仕様書バージョン番号：009) - 警察庁交通局運転免許課
   * https://www.npa.go.jp/laws/notification/koutuu/menkyo/menkyo20210630_150.pdf
