//削除チェックの関数
const releaseCheck = () => {
    // 「OK」時の処理開始 ＋ 確認ダイアログの表示
	if(window.confirm('本当にいいんですか？')){
		return true;
	}
	// 「OK」時の処理終了

	// 「キャンセル」時の処理開始
	else{
		window.alert('キャンセルされました'); // 警告ダイアログを表示
        return false;
	}
	// 「キャンセル」時の処理終了
}