package com.ltk.phone.addressbook;

import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.ltk.phone.addressbook.dao.AddressDao;
import com.ltk.phone.addressbook.dao.AddressDaoOraclempl;
import com.ltk.phone.addressbook.dao.AddressVo;

public class PhoneAddessController {
	private static final String MEMU_INFO = "*****************************************\n"
			+ "*\t       전화번호 관리 프로그램        \t*\n" + "*****************************************";
	private static final String MENU_SELECT = "1.리스트\t2.등록\t3.삭제\t4.검색\t5.종료\n"
			+ "--------------------------------------\n" + ">메뉴번호: ";
	private static final String MENU_SHOWINFO_MESSAGE = "<1.리스트>";
	private static final String MENU_ADDINFO_MESSAGE = "<2.이름>";
	private static final String MENU_DELETE_MESSAGE = "<3.삭제>";
	private static final String MENU_SEARCH_MESSAGE = "<4.검색>";

	private static final String INPUT_ERROR = "[다시입력해주세요]";
	private static final String INPUT_NUMBERFORMAT_ERROR = "[숫자를 입력해 주세요]";
	private static final String INPUT_NAME = ">이름: ";
	private static final String INPUT_NUMBER = ">번호: ";

	private static final String INPUT_PHONENUMBER = ">휴대전화: ";
	private static final String INPUT_HOUSENUMBER = ">집전화: ";

	private static final String ADD_SUCCESS_MESSAGE = "[등록되었습니다.]";
	private static final String ADD_ERROR_MESSAGE = "[등록을 실패했습니다.]";

	private static final String DELETE_SUCCESS_MESSAGE = "[삭제되었습니다.]";
	private static final String DELETE_ERROR_MESSAGE = "[삭제을 실패했습니다.]";
	private static final String SEARCH_EMPTY_MESSAGE = "[아무것도 못찾았습니다.]";
	private static final String END_MESSAGE = "*****************************************\n" + "*\t\t감사합니다\t\t\t*\n"
			+ "*****************************************";

	public static void start() {
		program();
		System.out.println(END_MESSAGE);
	}

	private static void program() {
		Scanner input = new Scanner(System.in);
		System.out.println(MEMU_INFO);
		boolean end = false;
		while (!end) {
			System.out.print(MENU_SELECT);
			try {
				int menu = Integer.parseInt(input.nextLine());
				System.out.println();

				switch (menu) {
				case 1:
					showNumberInfo();
					break;
				case 2:
					addNumberInfo(input);
					break;
				case 3:
					deleteNumberInfo(input);
					break;
				case 4:
					searchNumberInfo(input);
					break;
				case 5:
					end = true;
					break;
				default:
					System.err.println(INPUT_ERROR);
				}

				System.out.println();
			} catch (NumberFormatException e) {
				System.err.println(INPUT_NUMBERFORMAT_ERROR);
			}
		}
		input.close();
	}

	private static void showNumberInfo() {
		System.out.println(MENU_SHOWINFO_MESSAGE);

		AddressDao dao = new AddressDaoOraclempl();
		List<AddressVo> list = dao.getList();

		Iterator<AddressVo> it = list.iterator();

		if (!showList(it))
			System.out.println(SEARCH_EMPTY_MESSAGE);
	}

	private static void addNumberInfo(Scanner input) {
		System.out.println(MENU_ADDINFO_MESSAGE);
		System.out.print(INPUT_NAME);
		String name = input.nextLine();
		System.out.print(INPUT_PHONENUMBER);
		String tel = input.nextLine();
		System.out.print(INPUT_HOUSENUMBER);
		String hp = input.nextLine();

		AddressVo vo = new AddressVo(name, tel, hp);

		AddressDao dao = new AddressDaoOraclempl();
		boolean success = dao.insert(vo);

		System.out.println(success ? ADD_SUCCESS_MESSAGE : ADD_ERROR_MESSAGE);
	}

	private static void deleteNumberInfo(Scanner input) {
		System.out.println(MENU_DELETE_MESSAGE);
		System.out.printf(INPUT_NUMBER);
		try {
			Long target = Long.parseLong(input.nextLine());
			AddressDao dao = new AddressDaoOraclempl();

			boolean success = dao.delete(target);

			System.out.println((success ? DELETE_SUCCESS_MESSAGE : DELETE_ERROR_MESSAGE));
		} catch (NumberFormatException e) {
			System.err.println(INPUT_NUMBERFORMAT_ERROR);
		}
	}

	private static void searchNumberInfo(Scanner input) {
		System.out.println(MENU_SEARCH_MESSAGE);
		System.out.printf(INPUT_NAME);

		String target = input.nextLine();
		AddressDao dao = new AddressDaoOraclempl();
		
		List<AddressVo> list = dao.search(target);
		Iterator<AddressVo> it = list.iterator();

		showList(it);
	}

	private static boolean showList(Iterator<AddressVo> it) {
		boolean empty = false;
		while (it.hasNext()) {
			AddressVo vo = it.next();
			System.out.println(vo);
			empty = true;
		}
		return empty;
	}

}
