local $/; $_ = <>;
s/<script\b.*?<\/script>//gs; s/<style\b.*?<\/style>//gs;
s/.*?<main\b//s; s/<\/main>.*//s;
s/<\/(p|li|h[1-6]|div|tr)>/\n/g; s/<br\s*\/?>/\n/g; s/<li[^>]*>/- /g;
s/<[^>]+>//g;
s/&#x([0-9a-fA-F]+);/chr(hex($1))/ge; s/&#(\d+);/chr($1)/ge;
s/&nbsp;/ /g; s/&amp;/&/g; s/&quot;/"/g; s/&lt;/</g; s/&gt;/>/g;
s/[ \t]+/ /g; s/\n\s*\n+/\n/g;
binmode STDOUT, ':utf8'; print;
