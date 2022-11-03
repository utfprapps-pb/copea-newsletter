--drop function get_html_files_with_cid_instead_base64;
--drop type html_files_with_cid_instead_base64;
--drop type html_files_base64;

create type html_files_base64 as (content_id varchar(20), type_file varchar(20), justbase64 text);
create type html_files_with_cid_instead_base64 as (html_files html_files_base64[], html_with_content_id_instead_base64 text);

create or replace function get_html_files_with_cid_instead_base64(html text)
RETURNS html_files_with_cid_instead_base64
language plpgsql
as $$
declare
type_html_files_base64 html_files_base64;
	type_html_files_with_cid_instead_base64 html_files_with_cid_instead_base64;
	array_html_files_base64 html_files_base64[];
	content_id varchar(20);
	justbase64 text;
	increment_number integer;
	existsbase64 bool;
begin
	increment_number := 0;
	existsbase64 := true;
	while (existsbase64) loop
		justbase64 := regexp_replace(substring(html, '[a-zA-Z/:]+;base64,[a-zA-Z0-9+\/=]+'), '[a-zA-Z/:]+;base64,', '');
		existsbase64 := (justbase64 <> '');
		if (existsbase64) then
			content_id := 'file' || (increment_number + 1)::char(1);

			type_html_files_base64.content_id := content_id;
			type_html_files_base64.type_file := replace(substring(html, '[a-zA-Z]+;base64'), ';base64', '');
			type_html_files_base64.justbase64 := justbase64;

			array_html_files_base64[increment_number] := type_html_files_base64;

			increment_number := increment_number + 1;

			--troca o base64 pelo content-id
			html := regexp_replace(html, '[a-zA-Z/:]+;base64,[a-zA-Z0-9+\/=]+', 'cid:' || content_id);
end if;
end loop;

	if (array_html_files_base64 is null) then
		return null;
end if;

	type_html_files_with_cid_instead_base64.html_files := array_html_files_base64;
	type_html_files_with_cid_instead_base64.html_with_content_id_instead_base64 := html;

return type_html_files_with_cid_instead_base64;
end;
$$
