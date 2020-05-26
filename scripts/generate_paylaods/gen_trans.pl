#!/usr/bin/perl -w
use Data::UUID;
open(SHOPS,"shops.txt");
my $int=1;
my $token=$ENV{'TOKEN'}; 
print "token is $token";
my @shops;
while(my $line=<SHOPS>)
{
chomp $line;
push @shops, $line;
}

open(ACC,"accounts.txt");
while (my $line=<ACC>)
{
chomp $line;
my $filename=&gen_payload();
my $cmd=<<EOF
curl -H "Content-Type: application/json" -H "Authorization: Bearer $token" --data \@$filename http://localhost:8088/dio-au/v1/brand/abaaef28-e212-4532-8223-c875e80692ac/branch/e93331de-3de6-4401-9429-b166e983304c/bank-account/$line/transaction

EOF
;
system($cmd);
#print $cmd
}

sub gen_payload()
{
my $range = 100000;
my $minimum = 500;
my $amt = (int(rand($range))+ $minimum)/100;
my $desc = $shops[int( rand(@shops))];
my $ug    = Data::UUID->new;
my $uuid1 = $ug->create();
my $uuid = lc($ug->to_string($uuid1));

my $PAYLOAD=<<EOF
{
  "id": "$uuid",
  "schemeType": "CDR_BANKING",
  "type": "TRANSFER_INCOMING",
  "status": "POSTED",
  "description": "$desc",
  "originated": "2020-02-18T00:36:28.025Z",
  "posted": "2020-02-28T00:36:28.025Z",
  "applied": "2020-03-11T00:36:28.025Z",
  "amount": $amt,
  "currency": "AUD",
  "reference": "$desc",
  "apcs": {
    "schemeType": "CDR_BANKING",
    "branch": {
        "id": "e93331de-3de6-4401-9429-b166e983304c",
        "schemeType": "DIO_BANKING",
        "bsb": 999999,
        "bankName": "Brand One",
        "branchName": "Brand 1",
        "branchAddress": "1 Brand Street",
        "branchCity": "Brand 1 City",
        "branchState": "NSW",
        "branchPostcode": "2000"
    }
  }
}
EOF
;
my $file = "body_".$int.".json";
$int=$int+1;
open(my $json, ">",$file) ||die "can not open file for writing: $file $!\n";
print $json $PAYLOAD;
close $json;
return $file;
}
