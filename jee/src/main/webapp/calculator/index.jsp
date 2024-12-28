<html>
<head>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
  $(document).ready(function() {
        $('#calculator').submit(function() {
            $form = $(this);
            $operator = $('select#operator').val();
            $.post(
                $operator,
                $form.serialize(),
                function(responseText) {
                    if (!isNaN(responseText)) {
                        $('#result').text(responseText);
                        $('#error').text('');
                    } else {
                        $('#error').text(responseText);
                        $('#result').text('');
                    }
                }
            );
            return false;
        });
    });
</script>
</head>
<body>


<h2>Calculator</h2>
<form id="calculator" action="calculator" method="post">
        <input name="left">
        <select id="operator" name="operator">
           <option value="add"> + </option>
           <option value="subtract"> - </option>
           <option value="multiply"> * </option>
           <option value="div"> / </option>
       </select>
        <input name="right">
    <button type="submit"> Calculate </button>
    <p>Result: <span id="result">${result}</span></p>
    <p>Error: <span id="error">${error}</span></p>
</form>

</body>
</html>