ksbysample-webapp-email
=======================

Spring Boot でのメール送信機能と Doma 2 を試したいので、下記の概要の Webアプリケーションを作成してみます。

* 以下の機能を作ります。
    * メール送信機能
        * メール送信画面から From, To, Subject, 本文を入力してメールを送信します。
        * 本文は画面上から一部の項目のみ入力して Velocity のテンプレートファイルに当てはめて本文全体を生成します。
        * 送信したメールは DB に保存します。
    * 送信済メール検索機能
        * DB に保存された送信済メールを Subject, 本文で検索、一覧表示します。
* Project 名は ksbysample-webapp-email とします。
* 構成要素は、Spring Boot + Spring MVC + Thymeleaf + Doma 2 + Velocity ( メールのテンプレートに使用 ) です。
* DB は PostgreSQL 9.4.1 を PC にインストールして使用します。
* 実行環境は開発用 ( develop )、ユニットテスト用 ( unittest )、本番用 ( product ) の３種類を想定し、spring.profiles.active に指定された文字列で切り替えられるようにします。
* 画面は Twitter Bootstrap を使用して作成します。
* メールサーバは [smtp4dev](https://smtp4dev.codeplex.com/) を PC にインストールして使用します。
