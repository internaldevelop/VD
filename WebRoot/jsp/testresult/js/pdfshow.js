$(function() {
    var success = new PDFObject({ url: "ftl/测试报告模板1.pdf" }).embed("showpdf");
});
function showpdf(name)
{
	new PDFObject({ url: "ftl/"+name }).embed("showpdf");
}