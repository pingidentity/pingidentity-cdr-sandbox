#!/usr/bin/perl -w
use strict;
use Getopt::Std;
use File::Basename;
use File::Copy;
use Data::Dumper;

sub print_usage() {
	print "\nUsage: ". basename $0 ."-e <environment file> -i <input file> [-o <output file default is inputfile.subst> -h <help>]\n\n";

}

my %options = ();
getopts("e:i:o:h",\%options);
if ($options{h}) {
	&print_usage();
        exit -1;
}

my $env_file = $options{e};
my $in_file = $options{i};
my $out_file;
if ($options{o}) {
	$out_file = $options{o};
}
else {
	$out_file = $in_file.".subst";
}

if (! -f $env_file || ! -f $in_file) {
	&print_usage();
	exit -2;
}

my %env_vars_r;

open(ENV_FILE, "$env_file") || die "\nError can not open env file: $env_file\n\n";
open(IN_FILE, "$in_file") || die "\nError can not open input file: $in_file\n\n";

if (-f $out_file) {
	my $backup_file = $out_file."_".time();
	copy($out_file,$backup_file) or die "Copy failed: $!";
	print "\nBacked up file $out_file to $backup_file\n\n";

}

open(OUT_FILE, ">$out_file") || die "\nError can not open output file: $out_file\n\n";

# Create array for reverse subst
while(my $line=<ENV_FILE>)
{
	chomp $line;
	next if ($line !~/=/);
	my ($var, $val) = ($line=~ /(.*?)=(.*)/);
	#print "var =$var\n val=$val\n";
	$env_vars_r{$var} = $val if ($var && $val);
}

# Substitute values as required
while (my $line=<IN_FILE>)
{
	# Check for any $ variables from file first - we want to escape those.	
	my $dollar_key_value = '${DOLLAR}';
	$line =~s/\$/$dollar_key_value/eg;
	foreach my $key (sort {length($env_vars_r{$b}) <=> length($env_vars_r{$a})} keys %env_vars_r) 
	{
		next if ($key eq "DOLLAR");
   	#	print "-- $env_vars_r{$key} $key " . length($env_vars_r{$key}) . "\n";
		my $value = $env_vars_r{$key};
		#my $key_value = quotemeta('${'.$key."}");
		my $key_value = '${'.$key.'}';
		#print "key_value = $key_value\n";
		$line=~s/$value/$key_value/eg;	
	}
	print OUT_FILE $line;
}
print OUT_FILE "\n";
close OUT_FILE;
print "Successfuly created subst file: $out_file\n\n";
