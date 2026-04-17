import { useEffect, useRef, type JSX } from "react";

interface ModalProps {
	isOpen: boolean;
	onClose?: () => void;
	children: React.ReactNode;
}

export default function Modal(props: ModalProps): JSX.Element {
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

	function handleCloseModal(): void {
		if (props.onClose) {
			props.onClose();
		}
	}

	function handleKeyDown(
		event: React.KeyboardEvent<HTMLDialogElement>
	): void {
		if (event.key === "Escape") {
			handleCloseModal();
		}
	}

	return (
		<dialog ref={modalRef} onKeyDown={handleKeyDown}>
			{props.children}
		</dialog>
	);
}
