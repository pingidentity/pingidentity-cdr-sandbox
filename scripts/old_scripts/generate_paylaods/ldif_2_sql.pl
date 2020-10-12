#!/usr/bin/perl -w
use strict;
use Net::LDAP::LDIF;
use Net::LDAP::Entry;
use Data::UUID;

my $file="users.ldif"; 
my $ldif = Net::LDAP::LDIF->new( $file, "r", onerror => 'undef' );
my $ug    = Data::UUID->new;
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
my $uuid = lc($ug->create_from_name_str($id,$sn));
print <<EOF;
INSERT INTO CUSTOMER (ID, BRAND_ID) VALUES ('$id','abaaef28-e212-4532-8223-c875e80692ac');
INSERT INTO
  PERSON (
    ID,
    FIRST_NAME,
    LAST_NAME,
    OCCUPATION_CODE,
    PREFIX,
    CUSTOMER_ID
  )
VALUES
  (
   '$uuid',
    '$fn',
    '$sn',
    '0411',
    'MR',
    '$id'
  );

EOF
  }
}
$ldif->done ( );

