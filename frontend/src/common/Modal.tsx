import { useEffect, useRef, type JSX } from "react";

export type ModalProps = {
	isOpen: boolean;
	onClose?: () => void;
	children: React.ReactNode;
};

const Modal = (props: Readonly<ModalProps>): JSX.Element => {
	const modalRef = useRef<HTMLDialogElement>(null);

	useEffect(() => {
		const modalElement = modalRef.current;
		if (!modalElement) {
			return;
		}

		if (props.isOpen) {
			modalElement.showModal();
		} else {
			modalElement.close();
		}
	}, [props.isOpen]);

	const handleCloseModal = (): void => {
		if (props.onClose) {
			props.onClose();
		}
	};

	const handleKeyDown = (
		event: React.KeyboardEvent<HTMLDialogElement>
	): void => {
		if (event.key === "Escape") {
			handleCloseModal();
		}
	};

	return (
		<dialog ref={modalRef} onKeyDown={handleKeyDown}>
			{props.children}
		</dialog>
	);
};

export default Modal;
