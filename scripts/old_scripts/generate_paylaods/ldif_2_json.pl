#!/usr/bin/perl -w
use strict;
use Net::LDAP::LDIF;
use Net::LDAP::Entry;

my $file="users.ldif"; 
my $ldif = Net::LDAP::LDIF->new( $file, "r", onerror => 'undef' );
print "{\n";
while ( not $ldif->eof ( ) ) {
  my $entry = $ldif->read_entry ( );
  if ( $ldif->error ( ) ) {
    print "Error msg: ", $ldif->error ( ), "\n";
    print "Error lines:\n", $ldif->error_lines ( ), "\n";
  } else {
    # do stuff
my $id = $entry->get_value( "entryuuid" );
my $fn = $entry->get_value( "givenName" );
my $sn = $entry->get_value( "sn" );
print <<EOF;
 {
  "id": "$id",
  "schemeType": "DIO_COMMON",
  "customerType": "PERSON",
  "person": {
    "schemeType": "CDR_COMMON",
    "prefix": "MR",
    "firstName": "$fn",
    "lastName": "$sn",
    "middleNames": [
      "Test"
    ],
    "suffix": "II",
    "cdrCommon": {
      "occupationCode": "0411"
    }
  }
 }
EOF
  }
}
print "}\n";
$ldif->done ( );

